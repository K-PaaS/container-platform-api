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
    private static final String IS_ADMIN = "isAdmin";
    private static final String YAML_KEY = "yaml";
    private static final String NAMESPACE_KEY = "namespace";
    private static final String KIND_KEY = "kind";
    private static final String METADATA_KEY = "metadata";
    private static final String METADATA_NAME_KEY = "name";

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
     * @param joinPoint the joinPoint
     * @return the object
     * @throws Throwable
     */
    @Around("execution(* org.paasta.container.platform.api..*Controller.*(..))" + "&& !@annotation(org.paasta.container.platform.api.config.NoAuth)")
    public Object isAdminAspect(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] parameterValues = Arrays.asList(joinPoint.getArgs()).toArray();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<GrantedAuthority> list = (List<GrantedAuthority>) authentication.getAuthorities();
        LOGGER.info("YOUR AUTHORITY :: {}", list.get(0).getAuthority());
        String authority = list.get(0).getAuthority();

        boolean isAdmin = false;

        if(Constants.AUTH_CLUSTER_ADMIN.equals(authority)) {
            isAdmin = true;
        }

        CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
        String[] sigParamNames = methodSignature.getParameterNames();

        int index = 0;
        for (String name:sigParamNames) {
            if (IS_ADMIN.equals(name)) {
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
     * @param joinPoint the joinPoint
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
            if (YAML_KEY.equals(sigParamNames[i])) {
                yaml = Arrays.asList(parameterValues).get(i).toString();
            }

            if (NAMESPACE_KEY.equals(sigParamNames[i])) {
                namespace = Arrays.asList(parameterValues).get(i).toString();
            }
        }

        String requestResource;
        String requestURI = request.getRequestURI();

        if (StringUtils.isEmpty(namespace)) {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[3];
        } else {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        }

        requestResource = InspectionUtil.makeResourceName(requestResource);

        LOGGER.info("Creating Request Resource :: " + requestResource);

        String[] yamlArray = YamlUtil.splitYaml(yaml);
        boolean isExistResource = false;

        for (String temp : yamlArray) {
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, METADATA_KEY);
            String createYamlResourceName = YamlMetadata.get(METADATA_NAME_KEY).toString();
            String createYamlResourceNamespace;

            if (YamlMetadata.get(NAMESPACE_KEY) != null) {
                createYamlResourceNamespace = YamlMetadata.get(NAMESPACE_KEY).toString();
            } else {
                createYamlResourceNamespace = null;
            }

            if (StringUtils.isNotEmpty(createYamlResourceName) && StringUtils.isNotEmpty(createYamlResourceNamespace)) {
                if (createYamlResourceName.startsWith("kube") || createYamlResourceNamespace.startsWith("kube")) {
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.PREFIX_KUBE_NOT_ALLOW, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.PREFIX_KUBE_NOT_ALLOW);
                } else {
                    break;
                }
            } else if (StringUtils.isNotEmpty(createYamlResourceName) && StringUtils.isEmpty(createYamlResourceNamespace)) {
                if (createYamlResourceName.startsWith("kube")) {
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.PREFIX_KUBE_NOT_ALLOW, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.PREFIX_KUBE_NOT_ALLOW);
                } else {
                    break;
                }
            }
        }

        for (String temp : yamlArray) {
            String YamlKind = YamlUtil.parsingYaml(temp, KIND_KEY);
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, METADATA_KEY);

            String createYamlResourceName = YamlMetadata.get(METADATA_NAME_KEY).toString();

            if (YamlKind.equals(Constants.RESOURCE_POD)) {
                for (String na : NOT_ALLOWED_POD_NAME_LIST) {
                    if (createYamlResourceName.equals(na)) {
                        return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.NOT_ALLOWED_POD_NAME, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.NOT_ALLOWED_POD_NAME);
                    }
                }
            } else {
                break;
            }
        }

        for (String temp : yamlArray) {
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, METADATA_KEY);
            String createYamlResourceNamespace;

            if (YamlMetadata.get(NAMESPACE_KEY) != null) {
                createYamlResourceNamespace = YamlMetadata.get(NAMESPACE_KEY).toString();

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
            String kind = YamlUtil.parsingYaml(temp, KIND_KEY);

            String resourceKind = YamlUtil.makeResourceNameYAML(kind);

            if (resourceKind.equals(requestResource)) {
                isExistResource = true;
                break;
            }
        }

        if (!isExistResource) {
            return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.NOT_EXIST_RESOURCE, CommonStatusCode.BAD_REQUEST.getCode(),
                    requestResource + MessageConstant.NOT_EXIST);
        }

        for (String temp : yamlArray) {
            String resourceKind = YamlUtil.parsingYaml(temp, KIND_KEY);

            Object dryRunResult = InspectionUtil.resourceDryRunCheck("CreateUrl", namespace, resourceKind, temp, null);
            ObjectMapper oMapper = new ObjectMapper();
            ResultStatus createdRs = oMapper.convertValue(dryRunResult, ResultStatus.class);

            if (Constants.RESULT_STATUS_FAIL.equals(createdRs.getResultCode())) {
                LOGGER.info("DryRun :: Not valid yaml ");
                return createdRs;
            }

        }

        return joinPoint.proceed(parameterValues);
    }

    /**
     * API URL 호출 시 update 메소드인 경우 메소드 수행 전 처리 (do preprocessing, if update method is)
     *
     * @param joinPoint the joinPoint
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
            if (YAML_KEY.equals(sigParamNames[i])) {
                yaml = Arrays.asList(parameterValues).get(i).toString();
            }

            if (NAMESPACE_KEY.equals(sigParamNames[i])) {
                namespace = Arrays.asList(parameterValues).get(i).toString();
            }

            if ("resourceName".equals(sigParamNames[i])) {
                resourceName = Arrays.asList(parameterValues).get(i).toString();
            }
        }

        String requestResource;
        String requestURI = request.getRequestURI();

        if (StringUtils.isEmpty(resourceName)) {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[3];
            resourceName = namespace;
        } else {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        }

        requestResource = InspectionUtil.makeResourceName(requestResource);

        String resourceKind = YamlUtil.parsingYaml(yaml, KIND_KEY);
        resourceKind = YamlUtil.makeResourceNameYAML(resourceKind);

        String[] yamlArray = YamlUtil.splitYaml(yaml);
        for (String temp : yamlArray) {
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, METADATA_KEY);
            String updateYamlResourceName = YamlMetadata.get(METADATA_NAME_KEY).toString();
            String updateYamlResourceNamespace;

            if (YamlMetadata.get(NAMESPACE_KEY) != null) {
                updateYamlResourceNamespace = YamlMetadata.get(NAMESPACE_KEY).toString();
            } else {
                updateYamlResourceNamespace = null;
            }

            if (StringUtils.isNotEmpty(updateYamlResourceName) && StringUtils.isNotEmpty(updateYamlResourceNamespace)) {
                if (updateYamlResourceName.startsWith("kube") || updateYamlResourceNamespace.startsWith("kube")) {
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.PREFIX_KUBE_NOT_ALLOW, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.PREFIX_KUBE_NOT_ALLOW);
                } else {
                    break;
                }
            } else if (StringUtils.isNotEmpty(updateYamlResourceName) && StringUtils.isEmpty(updateYamlResourceNamespace)) {
                if (updateYamlResourceName.startsWith("kube")) {
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.PREFIX_KUBE_NOT_ALLOW, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.PREFIX_KUBE_NOT_ALLOW);
                } else {
                    break;
                }
            }
        }

        for (String temp : yamlArray) {
            String YamlKind = YamlUtil.parsingYaml(temp, KIND_KEY);
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, METADATA_KEY);

            String createYamlResourceName = YamlMetadata.get(METADATA_NAME_KEY).toString();

            if (YamlKind.equals(Constants.RESOURCE_POD)) {
                for (String na : NOT_ALLOWED_POD_NAME_LIST) {
                    if (createYamlResourceName.equals(na)) {
                        return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.NOT_ALLOWED_POD_NAME, CommonStatusCode.UNPROCESSABLE_ENTITY.getCode(), MessageConstant.NOT_ALLOWED_POD_NAME);
                    }
                }
            } else {
                break;
            }
        }

        for (String temp : yamlArray) {
            Map YamlMetadata = YamlUtil.parsingYamlMap(temp, METADATA_KEY);
            String createYamlResourceNamespace;

            if (YamlMetadata.get(NAMESPACE_KEY) != null) {
                createYamlResourceNamespace = YamlMetadata.get(NAMESPACE_KEY).toString();

                if (namespace.equals(createYamlResourceNamespace)) {
                    break;
                } else {
                    LOGGER.info("the namespace of the provided object does not match the namespace sent on the request':::::::::error");
                    return new ResultStatus(Constants.RESULT_STATUS_FAIL, CommonStatusCode.BAD_REQUEST.name(), CommonStatusCode.BAD_REQUEST.getCode(), MessageConstant.NOT_MATCH_NAMESPACES);
                }
            } else {
                break;
            }

        }

        String updateYamlResourceName = YamlUtil.parsingYaml(yaml, METADATA_KEY);

        if (!requestResource.equals(resourceKind) ) {
            return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.NOT_EXIST_RESOURCE, CommonStatusCode.BAD_REQUEST.getCode(), requestResource + MessageConstant.NOT_EXIST);
        }

        if (!resourceName.equals(updateYamlResourceName)) {
            return new ResultStatus(Constants.RESULT_STATUS_FAIL,
                    MessageConstant.NOT_ALLOWED_RESOURCE_NAME, CommonStatusCode.BAD_REQUEST.getCode(), resourceName + MessageConstant.RESOURCE_NAMED + requestResource + MessageConstant.NOT_UPDATE_YAML);
        }

        resourceKind = YamlUtil.parsingYaml(yaml, KIND_KEY);

        if (StringUtils.isNotEmpty(resourceKind) && StringUtils.isNotEmpty(yaml)) {
            Object dryRunResult = InspectionUtil.resourceDryRunCheck("UpdateUrl", namespace, resourceKind, yaml, resourceName);
            ObjectMapper oMapper = new ObjectMapper();
            ResultStatus updatedRs = oMapper.convertValue(dryRunResult, ResultStatus.class);
            if (Constants.RESULT_STATUS_FAIL.equals(updatedRs.getResultCode())) {
                LOGGER.info("DryRun :: Not valid yaml ");
                return updatedRs;
            }
        }

        return joinPoint.proceed(joinPoint.getArgs());
    }
}
