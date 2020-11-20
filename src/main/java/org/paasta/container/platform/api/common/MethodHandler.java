package org.paasta.container.platform.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.InspectionUtil;
import org.paasta.container.platform.api.common.util.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.paasta.container.platform.api.common.Constants.NOT_ALLOWED_POD_NAME_LIST;

/**
 * Method Handler 클래스
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
     * API URL 호출 시 로그인한 사용자 정보로 admin/user 판별 (check that login user is admin or user)
     *
     * true/false 를 argument 안에 파라미터로 넣어줌
     * isAdmin으로 판별해서 true면 admin 서비스 호출
     *
     * @param joinPoint
     * @return the object
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*(..))" + "&& !@annotation(org.paasta.container.platform.api.config.NoAuth)")
    public Object isAdminAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] parameterValues = Arrays.asList(joinPoint.getArgs()).toArray();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> list = (List<GrantedAuthority>) authentication.getAuthorities();
        LOGGER.info("your authority >>> " + list.get(0).getAuthority());
        String authority = list.get(0).getAuthority();

        boolean isAdmin = false;

        if(Constants.AUTH_CLUSTER_ADMIN.equals(authority)) {
            isAdmin = true;
        }

        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String[] sigParamNames = methodSignature.getParameterNames();

        int index = 0;
        for (String name:sigParamNames) {
            LOGGER.info("index >>> {}, param name >>> {}", index, name);

            if ("isAdmin".equals(name)) {
                LOGGER.info("success index :: {}, isAdmin :: {}", index, isAdmin);
                break;
            }

            index++;
        }

        parameterValues = CommonUtils.modifyValue(parameterValues, index, isAdmin);
        return joinPoint.proceed(parameterValues);
    }


    /**
     * API URL 호출 시 create 메소드인 경우 메소드 수행 전 처리 (do preprocessing, if create method is)
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*create*(..))")
    public Object createResourceAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        String yaml = null;
        String namespace = null;

        Object[] parameterValues = Arrays.asList(joinPoint.getArgs()).toArray();

        // parameter name -> namespace, yaml
        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String[] sigParamNames = methodSignature.getParameterNames();

        // yaml 의 위치 파악
        for (int i = 0; i < sigParamNames.length; i++) {
            if ("yaml".equals(sigParamNames[i])) {
                yaml = Arrays.asList(parameterValues).get(i).toString();
                LOGGER.info("yaml >>> " + yaml);
            }

            if ("namespace".equals(sigParamNames[i])) {
                namespace = Arrays.asList(parameterValues).get(i).toString();
                LOGGER.info("namespace ::: " + namespace);
            }
        }

        String requestResource;
        String requestURI = request.getRequestURI();

        LOGGER.info("requestURI :::::::::" + requestURI);

        if (StringUtils.isEmpty(namespace)) {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[3];
        } else {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        }

        requestResource = InspectionUtil.makeResourceName(requestResource);

        LOGGER.info("requestResource for create:::::::::" + requestResource);

        String[] yamlArray = YamlUtil.splitYaml(yaml);
        boolean isExistResource = false;



        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, "metadata");
            String createYamlResourceName = YamlMetadata.get("name").toString();
            String createYamlResourceNamespace;

            if (YamlMetadata.get("namespace") != null) {
                createYamlResourceNamespace = YamlMetadata.get("namespace").toString();
            } else {
                createYamlResourceNamespace = null;
            }

            if (StringUtils.isNotEmpty(createYamlResourceName) && StringUtils.isNotEmpty(createYamlResourceNamespace)) {
                if (createYamlResourceName.startsWith("kube") || createYamlResourceNamespace.startsWith("kube")) {
                    LOGGER.info("The prefix 'kube-' is not allowed.':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.PREFIX_KUBE_NOT_ALLOW, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.PREFIX_KUBE_NOT_ALLOW);
                } else {
                    break;
                }
            } else if (StringUtils.isNotEmpty(createYamlResourceName) && StringUtils.isEmpty(createYamlResourceNamespace)) {
                if (createYamlResourceName.startsWith("kube")) {
                    LOGGER.info("The prefix 'kube-' is not allowed.':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.PREFIX_KUBE_NOT_ALLOW, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.PREFIX_KUBE_NOT_ALLOW);
                } else {
                    break;
                }
            }
        }

        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            String YamlKind = YamlUtil.parsingYaml(temp, "kind");
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, "metadata");

            String createYamlResourceName = YamlMetadata.get("name").toString();

            if (YamlKind.equals(Constants.RESOURCE_POD)) {
                for (String na : NOT_ALLOWED_POD_NAME_LIST) {
                    if (createYamlResourceName.equals(na)) {
                        LOGGER.info("This 'Pod' name is not allowed.':::::::::error");
                        return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.NOT_ALLOWED_POD_NAME, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.NOT_ALLOWED_POD_NAME);
                    }
                }
            } else {
                break;
            }
        }

        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, "metadata");
            String createYamlResourceNamespace;

            if (YamlMetadata.get("namespace") != null) {
                createYamlResourceNamespace = YamlMetadata.get("namespace").toString();

                if (namespace.equals(createYamlResourceNamespace)) {
                    break;
                } else {
                    LOGGER.info("the namespace of the provided object does not match the namespace sent on the request':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, CommonStatusCode.BAD_REQUEST.name(),
                            CommonStatusCode.BAD_REQUEST.getCode(), MessageConstant.NOT_MATCH_NAMESPACES);
                }
            } else {
                break;
            }

        }


        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            String kind = YamlUtil.parsingYaml(temp, "kind");

            String resourceKind = YamlUtil.makeResourceNameYAML(kind);

            if (resourceKind.equals(requestResource)) {
                LOGGER.info("resource include:::::::::" + resourceKind);
                isExistResource = true;
                break;
            }
        }

        if (!isExistResource) {
            LOGGER.info("The corresponding resource does not exist:::::::::error");
            //return  new ErrorMessage(Constants.RESULT_STATUS_FAIL, "The corresponding resource does not exist", 400, "Resource Kind '"+requestResource+"' does not exist" );
            return new ResultStatus(Constants.RESULT_STATUS_FAIL, "The corresponding resource does not exist", 400, "Resource Kind '" + requestResource + "' does not exist.");
        }

        for (String temp : yamlArray) {

            String resourceKind = YamlUtil.parsingYaml(temp, "kind");
            LOGGER.info("dryRun resourceKind :::::::::" + resourceKind);

            Object dryRunResult = InspectionUtil.resourceDryRunCheck("CreateUrl", namespace, resourceKind, temp, null);
            ObjectMapper oMapper = new ObjectMapper();
            ResultStatus createdRs = oMapper.convertValue(dryRunResult, ResultStatus.class);

            if (Constants.RESULT_STATUS_FAIL.equals(createdRs.getResultCode())) {
                LOGGER.info("dryRun :: not valid yaml ");
                return createdRs;
            }

        }

        return joinPoint.proceed(parameterValues);
    }

    /**
     * API URL 호출 시 update 메소드인 경우 메소드 수행 전 처리 (do preprocessing, if update method is)
     *
     * @param joinPoint
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*update*(..))")
    public Object updateResourceAspect(ProceedingJoinPoint joinPoint) throws Throwable {

        String yaml = null;
        String namespace = null;
        String resourceName = null;

        Object[] parameterValues = Arrays.asList(joinPoint.getArgs()).toArray();

        // parameter name -> namespace, yaml
        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String[] sigParamNames = methodSignature.getParameterNames();

        // yaml 의 위치 파악
        for (int i=0; i < sigParamNames.length; i++) {
            if ("yaml".equals(sigParamNames[i])) {
                yaml = Arrays.asList(parameterValues).get(i).toString();
                LOGGER.info("yaml >>> " + yaml);
            }

            if ("namespace".equals(sigParamNames[i])) {
                namespace = Arrays.asList(parameterValues).get(i).toString();
                LOGGER.info("namespace ::: " + namespace);
            }

            if ("resourceName".equals(sigParamNames[i])) {
                resourceName = Arrays.asList(parameterValues).get(i).toString();
                LOGGER.info("resourceName ::: " + resourceName);
            }
        }

        LOGGER.info("namespace >> {}, resourceName >> {}", namespace, resourceName);

        String requestResource;
        String requestURI = request.getRequestURI();

        if (StringUtils.isEmpty(resourceName)) {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[3];
            resourceName = namespace;
        } else {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        }

        requestResource = InspectionUtil.makeResourceName(requestResource);

        String resourceKind = YamlUtil.parsingYaml(yaml, "kind");
        resourceKind = YamlUtil.makeResourceNameYAML(resourceKind);


        String[] yamlArray = YamlUtil.splitYaml(yaml);
        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, "metadata");
            String updateYamlResourceName = YamlMetadata.get("name").toString();
            String updateYamlResourceNamespace;

            if (YamlMetadata.get("namespace") != null) {
                updateYamlResourceNamespace = YamlMetadata.get("namespace").toString();
            } else {
                updateYamlResourceNamespace = null;
            }

            if (StringUtils.isNotEmpty(updateYamlResourceName) && StringUtils.isNotEmpty(updateYamlResourceNamespace)) {
                if (updateYamlResourceName.startsWith("kube") || updateYamlResourceNamespace.startsWith("kube")) {
                    LOGGER.info("The prefix 'kube-' is not allowed.':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, "The prefix 'kube-' is not allowed.", 422, "The prefix 'kube-' is not allowed.");
                } else {
                    break;
                }
            } else if (StringUtils.isNotEmpty(updateYamlResourceName) && StringUtils.isEmpty(updateYamlResourceNamespace)) {
                if (updateYamlResourceName.startsWith("kube")) {
                    LOGGER.info("The prefix 'kube-' is not allowed.':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, "The prefix 'kube-' is not allowed.", 422, "The prefix 'kube-' is not allowed.");
                } else {
                    break;
                }
            }
        }

        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            String YamlKind = YamlUtil.parsingYaml(temp, "kind");
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, "metadata");

            String createYamlResourceName = YamlMetadata.get("name").toString();

            if (YamlKind.equals(Constants.RESOURCE_POD)) {
                for (String na : NOT_ALLOWED_POD_NAME_LIST) {
                    if (createYamlResourceName.equals(na)) {
                        LOGGER.info("This 'Pod' name is not allowed.':::::::::error");
                        return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.NOT_ALLOWED_POD_NAME, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.NOT_ALLOWED_POD_NAME);
                    }
                }
            } else {
                break;
            }
        }

        for (String temp : yamlArray) {
            LOGGER.info("temp:::::::::" + temp);
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, "metadata");
            String createYamlResourceNamespace;

            if (YamlMetadata.get("namespace") != null) {
                createYamlResourceNamespace = YamlMetadata.get("namespace").toString();

                if (namespace.equals(createYamlResourceNamespace)) {
                    break;
                } else {
                    LOGGER.info("the namespace of the provided object does not match the namespace sent on the request':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, "BadRequest", 400, "the namespace of the provided object does not match the namespace sent on the request");
                }
            } else {
                break;
            }

        }


        String updateYamlResourceName = YamlUtil.parsingYaml(yaml, "metadata");

        if (!requestResource.equals(resourceKind) ) {
            LOGGER.info("The corresponding resource does not exist:::::::::error");
            //return  new ErrorMessage(Constants.RESULT_STATUS_FAIL, "The corresponding resource does not exist", 400, "Resource Kind '"+requestResource+"' does not exist." );
            return new ResultStatus(Constants.RESULT_STATUS_FAIL, "The corresponding resource does not exist", 400, "Resource Kind '"+ requestResource +"' does not exist." );
        }

        if (!resourceName.equals(updateYamlResourceName)) {
            LOGGER.info("Resource name is invalid:::::::::error");
            //return  new ErrorMessage(Constants.RESULT_STATUS_FAIL, "Resource name is invalid.", 400, "This is not an update yaml for the " + requestResource + " name '"+ resourceName + "'." );
            return new ResultStatus(Constants.RESULT_STATUS_FAIL,
                    "Resource name is invalid.", 400, "This is not an update yaml for the " + requestResource + " name '"+ resourceName + "'." );
        }

        resourceKind = YamlUtil.parsingYaml(yaml, "kind");

        if (StringUtils.isNotEmpty(resourceKind) && StringUtils.isNotEmpty(yaml)) {
            Object dryRunResult = InspectionUtil.resourceDryRunCheck("UpdateUrl", namespace, resourceKind, yaml, resourceName);
            ObjectMapper oMapper = new ObjectMapper();
            ResultStatus updatedRs = oMapper.convertValue(dryRunResult, ResultStatus.class);
            if (Constants.RESULT_STATUS_FAIL.equals(updatedRs.getResultCode())) {
                LOGGER.info("dryRun :: not valid yaml ");
                return updatedRs;
            }
        }



        return joinPoint.proceed(joinPoint.getArgs());
    }
}
