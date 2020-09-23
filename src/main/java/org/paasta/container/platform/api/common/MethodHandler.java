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
 * AOP - Common Create/Update resource
 *
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


        Object[] methodArguments = joinPoint.getArgs();
        String namespace = (String) methodArguments[1];
        String yaml = (String) methodArguments[2];

        String requestURI = request.getRequestURI();
        LOGGER.info("requestURI :::::::::" + requestURI);

        String requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        requestResource = InspectionUtil.makeResourceName(requestResource);

        LOGGER.info("requestResource for create:::::::::" + requestResource);


        String[] yamlArray = YamlUtil.splitYaml(yaml);
        boolean  isExistResource = false;


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
            LOGGER.info("The corresponding resource does not exist:::::::::error");
            return  new ErrorMessage(Constants.RESULT_STATUS_FAIL,
                    "The corresponding resource does not exist", 400, "Resource Kind '"+requestResource+"' does not exist" );
        }

        for (String temp : yamlArray) {

            String resourceKind = YamlUtil.parsingYaml(temp, "kind");
            LOGGER.info("dryRun resourceKind :::::::::" + resourceKind);

            Object dryRunResult = InspectionUtil.resourceDryRunCheck("Create", namespace, resourceKind, temp, null);
            ObjectMapper oMapper = new ObjectMapper();
            Map map = oMapper.convertValue(dryRunResult, Map.class);

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

        Object[] methodArguments = joinPoint.getArgs();
        String namespace = (String) methodArguments[1];

        // resourceName :: ex) nginx
        String resourceName = (String) methodArguments[2];

        // yaml for update resource
        String yaml = (String) methodArguments[3];

        LOGGER.info("namespace >> {}, resourceName >> {}", namespace, resourceName);

        String requestURI = request.getRequestURI();
        String requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        requestResource = InspectionUtil.makeResourceName(requestResource);

        String resourceKind = YamlUtil.parsingYaml(yaml, "kind");
        String updateYamlResourceName = YamlUtil.parsingYaml(yaml, "metadata");

        if(!requestResource.equals(resourceKind) ) {
            LOGGER.info("The corresponding resource does not exist:::::::::error");
            return  new ErrorMessage(Constants.RESULT_STATUS_FAIL,
                    "The corresponding resource does not exist", 400, "Resource Kind '"+requestResource+"' does not exist." );
        }


        if(!resourceName.equals(updateYamlResourceName)) {
            LOGGER.info("Resource name is invalid:::::::::error");
            return  new ErrorMessage(Constants.RESULT_STATUS_FAIL,
                    "Resource name is invalid.", 400, "This is not an update yaml for the " + requestResource + " name '"+ resourceName + "'." );
        }

        Object dryRunResult = InspectionUtil.resourceDryRunCheck("Update", namespace, resourceKind, yaml, resourceName);
        ObjectMapper oMapper = new ObjectMapper();
        Map map = oMapper.convertValue(dryRunResult, Map.class);

        if (Constants.RESULT_STATUS_FAIL.equals(map.get("resultCode"))) {
            LOGGER.info("dryRun :: not valid yaml ");
            return map;
        }

        return joinPoint.proceed(joinPoint.getArgs());
    }
}
