package org.paasta.container.platform.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.paasta.container.platform.api.common.util.InspectionUtil;
import org.paasta.container.platform.api.common.util.YamlUtil;
import org.paasta.container.platform.api.exception.ErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;

/**
 * @author hrjin
 * @version 1.0
 * @since 2020-08-25
 **/
@Aspect
@Component
public class MethodHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandler.class);
    private final HttpServletRequest request;

    @Autowired
    public MethodHandler(HttpServletRequest request) {
        this.request = request;
    }


    /**
     * API URL 호출 시 create 메소드인 경우 메소드 수행 전 처리
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*create*(..))")
    public Object createResourceAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] parameterValues = Arrays.asList(joinPoint.getArgs()).toArray();

        // 1. 파라미터 값 가져오기
        Object[] methodArguments = joinPoint.getArgs();
        String namespace = (String) methodArguments[1];
        String yaml = (String) methodArguments[2];

        // 2. requestURL 값 가져오기 * path에 고정된 Resource 가져오기
        String requestURI = request.getRequestURI();
        LOGGER.info("requestURI :::::::::" + requestURI);

        String requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        requestResource = InspectionUtil.makeResourceName(requestResource);

        LOGGER.info("requestResource for create:::::::::" + requestResource);

        // 3. yaml split 진행 '---' 로 구분
        String[] yamlArray = YamlUtil.splitYaml(yaml);
        boolean  isExistResource = false;

        // 4. split한 yaml 배열 중 해당 Resource가 포함됬는지 확인
        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            String kind = YamlUtil.parsingYaml(temp,"kind");

            if(kind.equals(requestResource)) {
                LOGGER.info("resource include:::::::::" + kind);
                isExistResource = true;
                break;
            }
        }

        if(!isExistResource) {
            // 없다면 에러메세지 발생
            LOGGER.info("isExistResource:::::::::error");
            return  new ErrorMessage(Constants.RESULT_STATUS_FAIL,
                    "The corresponding resource does not exist", 400, "Resource Kind '"+requestResource+"' does not exist" );
        }

        //4. 있다면 yaml 배열 for문 돌려서 수행하기 & 지정된 path가 하나라도 포함되어 있어서 여기까지온것이다.
        for (String temp : yamlArray) {

            // Resource Kind 파싱하기
            String resourceKind = YamlUtil.parsingYaml(temp, "kind");
            LOGGER.info("dryRun resourceKind :::::::::" + resourceKind);

            //DryRun 체크하기
            Object dryRunResult = InspectionUtil.resourceDryRunCheck("Create", namespace, resourceKind, temp, null);
            ObjectMapper oMapper = new ObjectMapper();
            Map map = oMapper.convertValue(dryRunResult, Map.class);

            //DryRun 체크하여 yaml이 not valid 할 경우 에러 메세지 return :: 여긴 그 리소스 path 로 dryRun 해줌
            if (Constants.RESULT_STATUS_FAIL.equals(map.get("resultCode"))) {
                LOGGER.info("dryRun :: not valid yaml ");
                return map;
            }

        }

        return joinPoint.proceed(parameterValues);
    }

    /**
     * API URL 호출 시 update 메소드인 경우 메소드 수행 전 처리
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*update*(..))")
    public Object updateResourceAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        // namespace, name, yaml
        // 1. namespace, name 2개를 가지고 get을 해온다.
        // 2. 넘어온 yaml의 1) kind와 각 리소스의 고유 이름인 2)metadata.name을 기존 것과 비교한다.
        // 3. 그 후의 유효성은 dryRun이 해줄거임.
        // 4. dryRun 통과 시 proceed
        // 5. Controller update쪽은 복합 yaml의 경우가 없는 거임.


        // 1. 파라미터 값 가져오기
        Object[] methodArguments = joinPoint.getArgs();
        String namespace = (String) methodArguments[1];

        // resourceName :: ex) nginx
        String resourceName = (String) methodArguments[2];

        // yaml for update resource
        String yaml = (String) methodArguments[3];

        LOGGER.info("namespace >> {}, resourceName >> {}", namespace, resourceName);

        // requestURL 값 가져오기 * path에 고정된 Resource 가져오기
        String requestURI = request.getRequestURI();
        String requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        requestResource = InspectionUtil.makeResourceName(requestResource);

        // Resource Kind 파싱하기
        String resourceKind = YamlUtil.parsingYaml(yaml, "kind");
        String updateYamlResourceName = YamlUtil.parsingYaml(yaml, "metadata");

        if(!requestResource.equals(resourceKind) ) {
            // 없다면 에러메세지 발생
            LOGGER.info("isExistResource:::::::::error");
            return  new ErrorMessage(Constants.RESULT_STATUS_FAIL,
                    "The corresponding resource does not exist", 400, "Resource Kind '"+requestResource+"' does not exist." );
        }


        if(!resourceName.equals(updateYamlResourceName)) {
            // 없다면 에러메세지 발생
            LOGGER.info("Resource name is different.:::::::::error");
            return  new ErrorMessage(Constants.RESULT_STATUS_FAIL,
                    "Resource name is invalid.", 400, "This is not an update yaml for the " + requestResource + " name '"+ resourceName + "'." );
        }

        //DryRun 체크하기
        Object dryRunResult = InspectionUtil.resourceDryRunCheck("Update", namespace, resourceKind, yaml, resourceName);
        ObjectMapper oMapper = new ObjectMapper();
        Map map = oMapper.convertValue(dryRunResult, Map.class);

        //DryRun 체크하여 yaml이 not valid 할 경우 에러 메세지 return :: 여긴 그 리소스 path 로 dryRun 해줌
        if (Constants.RESULT_STATUS_FAIL.equals(map.get("resultCode"))) {
            LOGGER.info("dryRun :: not valid yaml ");
            return map;
        }

        return joinPoint.proceed(joinPoint.getArgs());
    }
}
