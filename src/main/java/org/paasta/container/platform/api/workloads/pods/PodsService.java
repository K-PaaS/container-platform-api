package org.paasta.container.platform.api.workloads.pods;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.customServices.CustomServices;
import org.paasta.container.platform.api.workloads.deployments.Deployments;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * Pods Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.09
 */
@Service
public class PodsService {
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Pods service.
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public PodsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * Pod 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the pods list
     */
    /*PodsList getPodList(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (PodsList) commonService.setResultModel(commonService.setResultObject(resultMap, PodsList.class), Constants.RESULT_STATUS_SUCCESS);
    }*/
    public PodsList getPodsList(String namespace, int limit, String continueToken) {
        String param = "";

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (PodsList) commonService.setResultModel(commonService.setResultObject(responseMap, PodsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Selector를 이용해 Pod 목록을 조회한다.
     *
     * @param namespace the namespace
     * @param selector  the selector
     * @return the pod list
     */
    PodsList getPodListWithLabelSelector(String namespace, String selector) {
        String requestSelector = "?labelSelector=" + selector;
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        return (PodsList) commonService.setResultModel(commonService.setResultObject(resultMap, PodsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Node 이름을 이용해 Pod 목록을 조회한다.
     *
     * @param namespace               the namespace
     * @param nodeName                the node name
     * @return the pod list
     */
    PodsList getPodListByNode(String namespace, String nodeName) {
        String requestURL = propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace)
                + "/?fieldSelector=spec.nodeName=" + nodeName;

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API, requestURL,
                HttpMethod.GET, null, Map.class);
        return (PodsList) commonService.setResultModel(commonService.setResultObject(resultMap, PodsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 상세 정보를 조회한다.
     *
     * @param namespace the namespace
     * @param podsName   the pods name
     * @return the pods
     */
    public Pods getPods(String namespace, String podsName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsGetUrl().replace("{namespace}", namespace).replace("{name}", podsName),
                HttpMethod.GET, null, Map.class);

        return (Pods) commonService.setResultModel(commonService.setResultObject(responseMap, Pods.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods의 YAML을 조회한다.
     *
     * @param namespace the namespace
     * @param podName   the pods name
     * @param resultMap  the result map
     * @return the pods
     */
    public Pods getPodsYaml(String namespace, String podName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsGetUrl().replace("{namespace}", namespace).replace("{name}", podName),
                HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);
        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (Pods) commonService.setResultModel(commonService.setResultObject(resultMap, Pods.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return return is succeeded
     */
    public Object createPods(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, Pods.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS);
    }

    /**
     * Pods를 삭제한다.
     *
     * @param namespace        the namespace
     * @param resourceName the service name
     * @param resultMap the result map
     * @return the ResultStatus
     */
    public ResultStatus deletePods(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS);
    }

    /**
     * Pods를 수정한다.
     *
     * @param namespace     the namespace
     * @param name          the pods name
     * @param yaml          the yaml
     * @return the pods
     */
    public Object updatePods(String namespace, String name, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", name), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, Pods.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS_DETAIL.replace("{podName:.+}", name));
    }
}
