package org.paasta.container.platform.api.common.util;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.exception.ContainerPlatformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 넘어오는 Resource의 생성/수정 메소드를 호출하는 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.04
 **/
@Component
public class ResourceExecuteManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResourceExecuteManager.class);

    /**
     * make create Method
     *
     * @param kind
     * @return
     */
    public static String makeServiceMethodName(String kind) {
        return "create" + kind + "s";
    }


    /**
     * 넘어오는 Resource에 따라 각 Resource를 수행하는 메서드 호출
     *
     * @param namespace
     * @param kind
     * @param yaml
     * @return
     * @throws Exception
     */
    public static Object execServiceMethod(String namespace, String kind, String yaml) throws Exception {

        // 해당 전문의 서비스 처리 method 정보 조회
        String [] arrMethodInfo = Constants.RESOURCE_SERVICE_MAP.get(kind).split(":");
        String methodClassPackage = arrMethodInfo[0].trim();
        String methodClassName = arrMethodInfo[1].trim();
        String methodName = makeServiceMethodName(kind);

        //createServices
        LOGGER.info("method name >>> " + methodName);

        String injectBeanName = methodClassName.substring(0,1).toLowerCase() + methodClassName.substring(1);

        // 처리 메소드가 있는 서비스 클래스의 인스턴스
        Object targetObject = InspectionUtil.getBean(injectBeanName);

        // 처리 메소드 정보
        Method paramMethod = targetObject.getClass().getDeclaredMethod(methodName, String.class, String.class);
        if(paramMethod == null) {
            throw new ContainerPlatformException("처리할 메소드 (" + methodName + ") 가 존재 하지 않습니다.", "404");
        }

        LOGGER.info("method >>> " + paramMethod);

        // 해당 메소드를 호출한다.
        return paramMethod.invoke(targetObject, namespace, yaml);
    }

    /**
     * multi yaml일 때 Controller에서 yaml 순서대로 Resource 생성
     *
     * @param namespace
     * @param yaml
     * @return
     * @throws Exception
     */
    public static Object commonControllerExecute(String namespace, String yaml) throws Exception {
        String[] multiYaml;

        multiYaml = YamlUtil.splitYaml(yaml);
        Object object = null;

        //  우선 순위에 있는 kind 처리 loop
        for (String temp : multiYaml) {
            String kind = YamlUtil.parsingYaml(temp,"kind");
            object = execServiceMethod(namespace, kind, temp);
        }
        return object;
    }

}
