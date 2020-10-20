package org.paasta.container.platform.api.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.InspectionUtil;
import org.paasta.container.platform.api.common.util.YamlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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

        if (namespace == null || namespace.length() == 0) {
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
            String name = YamlUtil.parsingYaml(temp, "metadata");

            if (name.startsWith("kube") || namespace.startsWith("kube")) {
                LOGGER.info("The prefix 'kube-' is not allowed.':::::::::error");
                return new ResultStatus(Constants.RESULT_STATUS_FAIL, "The prefix 'kube-' is not allowed.", 400, "The prefix 'kube-' is not allowed.");
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
     * API URL 호출 시 update 메소드인 경우 메소드 수행 전 처리
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

        if (resourceName == null || resourceName.length() == 0) {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[3];
            resourceName = namespace;
        } else {
            requestResource = InspectionUtil.parsingRequestURI(requestURI)[5];
        }

        requestResource = InspectionUtil.makeResourceName(requestResource);//replicaset

        String resourceKind = YamlUtil.parsingYaml(yaml, "kind");//ReplicaSet
        resourceKind = YamlUtil.makeResourceNameYAML(resourceKind);//replicaset

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

        Object dryRunResult = InspectionUtil.resourceDryRunCheck("UpdateUrl", namespace, resourceKind, yaml, resourceName);
        ObjectMapper oMapper = new ObjectMapper();
        ResultStatus updatedRs = oMapper.convertValue(dryRunResult, ResultStatus.class);

        if (Constants.RESULT_STATUS_FAIL.equals(updatedRs.getResultCode())) {
            LOGGER.info("dryRun :: not valid yaml ");
            return updatedRs;
        }

        return joinPoint.proceed(joinPoint.getArgs());
    }
}
