package org.paasta.container.platform.api.workloads.pods;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.workloads.pods.support.ContainerStatusesItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * Instantiates a new Pods service
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
     * Pods 목록 조회(Get Pods list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the pods list
     */
    public PodsList getPodsList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);

        PodsList podsList = commonService.setResultObject(responseMap, PodsList.class);
        podsList = commonService.resourceListProcessing(podsList, offset, limit, orderBy, order, searchName, PodsList.class);

        return (PodsList) commonService.setResultModel(podsList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Pods 목록 조회(Get Pods list)
     * (Admin Portal)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the pods list
     */
    public Object getPodsListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        PodsListAdmin podsListAdmin = commonService.setResultObject(responseMap, PodsListAdmin.class);
        podsListAdmin = commonService.resourceListProcessing(podsListAdmin, offset, limit, orderBy, order, searchName, PodsListAdmin.class);

        for (PodsListAdminList po:podsListAdmin.getItems()) {

            if(po.getStatus().getContainerStatuses() == null) {
                List<ContainerStatusesItem> list = new ArrayList<>();
                ContainerStatusesItem item = new ContainerStatusesItem();
                item.setRestartCount(0);

                list.add(item);

                po.getStatus().setContainerStatuses(list);
            }
        }

        return commonService.setResultModel(podsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 목록 조회(Get Pods selector)
     *
     * @param namespace the namespace
     * @param selector  the selector
     * @return the pods list
     */
    PodsList getPodListWithLabelSelector(String namespace, String selector) {
        String requestSelector = "?labelSelector=" + selector;
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        PodsList podsList = commonService.setResultObject(resultMap, PodsList.class);
        podsList = commonService.setCommonItemMetaDataBySelector(podsList, PodsList.class);

        return (PodsList) commonService.setResultModel(podsList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 목록 조회(Get Pods selector)
     * (Admin portal)
     *
     * @param namespace the namespace
     * @param selector  the selector
     * @return the pods list
     */
    public PodsListAdmin getPodListWithLabelSelectorAdmin(String namespace, String selector) {
        String requestSelector = "?labelSelector=" + selector;
        HashMap resultMap = (HashMap) restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        PodsListAdmin podsListAdmin = commonService.setResultObject(resultMap, PodsListAdmin.class);
        podsListAdmin = commonService.setCommonItemMetaDataBySelector(podsListAdmin, PodsListAdmin.class);

        return (PodsListAdmin) commonService.setResultModel(podsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 목록 조회(Get Pods node)
     *
     * @param namespace the namespace
     * @param nodeName  the node name
     * @return the pods list
     */
    PodsList getPodListByNode(String namespace, String nodeName) {
        String requestURL = propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace)
                + "/?fieldSelector=spec.nodeName=" + nodeName;

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API, requestURL,
                HttpMethod.GET, null, Map.class);

        PodsList podsList = commonService.setResultObject(resultMap, PodsList.class);
        podsList = commonService.setCommonItemMetaDataBySelector(podsList, PodsList.class);

        return (PodsList) commonService.setResultModel(podsList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 목록 조회(Get Pods node)
     * (Admin portal)
     *
     * @param namespace the namespace
     * @param nodeName  the node name
     * @return the pods list
     */
    public PodsListAdmin getPodListByNodeAdmin(String namespace, String nodeName) {
        String requestURL = propertyService.getCpMasterApiListPodsListUrl().replace("{namespace}", namespace)
                + "/?fieldSelector=spec.nodeName=" + nodeName;

        HashMap resultMap = (HashMap) restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, requestURL,
                HttpMethod.GET, null, Map.class);

        PodsListAdmin podsListAdmin = commonService.setResultObject(resultMap, PodsListAdmin.class);
        podsListAdmin = commonService.setCommonItemMetaDataBySelector(podsListAdmin, PodsListAdmin.class);

        return (PodsListAdmin) commonService.setResultModel(podsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 상세 조회(Get Pods detail)
     *
     * @param namespace the namespace
     * @param podsName  the pods name
     * @return the pods detail
     */
    public Pods getPods(String namespace, String podsName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsGetUrl().replace("{namespace}", namespace).replace("{name}", podsName),
                HttpMethod.GET, null, Map.class);

        return (Pods) commonService.setResultModel(commonService.setResultObject(responseMap, Pods.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 상세 조회(Get Pods detail)
     * (Admin Portal)
     *
     * @param namespace the namespace
     * @param podsName  the pods name
     * @return the pods detail
     */
    public Object getPodsAdmin(String namespace, String podsName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", podsName)
                , HttpMethod.GET, null, Map.class);
        HashMap responseMap;

        try {
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, PodsAdmin.class), Constants.RESULT_STATUS_SUCCESS);

    }

    /**
     * Pods YAML 조회(Get Pods yaml)
     *
     * @param namespace the namespace
     * @param podName   the pods name
     * @param resultMap the result map
     * @return the pods yaml
     */
    public PodsYaml getPodsYaml(String namespace, String podName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsGetUrl().replace("{namespace}", namespace).replace("{name}", podName),
                HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);
        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (PodsYaml) commonService.setResultModel(commonService.setResultObject(resultMap, PodsYaml.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 생성(Create Pods)
     *
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    public Object createPods(String namespace, String yaml, boolean isAdmin) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class, isAdmin);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, Pods.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS);
    }

    /**
     * Pods 삭제(Delete Pods)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param isAdmin      the isAdmin
     * @return return is succeeded
     */
    public ResultStatus deletePods(String namespace, String resourceName, boolean isAdmin) {
        ResultStatus resultStatus;

        if(isAdmin) {
            resultStatus = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListPodsDeleteUrl()
                            .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);
        } else {
            resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListPodsDeleteUrl()
                            .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS);
    }

    /**
     * Pods 수정(Update Pods)
     *
     * @param namespace the namespace
     * @param name      the pods name
     * @param yaml      the yaml
     * @return return is succeeded
     */
    public Object updatePods(String namespace, String name, String yaml, boolean isAdmin) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", name), HttpMethod.PUT, yaml, Object.class, isAdmin);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, Pods.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS_DETAIL.replace("{podName:.+}", name));
    }


    /**
     * 전체 Namespaces 의 Pods Admin 목록 조회(Get Services Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the pods all list
     */
    public Object getPodsListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPodsListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        PodsListAdmin podsListAdminList = commonService.setResultObject(responseMap, PodsListAdmin.class);
        podsListAdminList = commonService.resourceListProcessing(podsListAdminList, offset, limit, orderBy, order, searchName, PodsListAdmin.class);

        for (PodsListAdminList po:podsListAdminList.getItems()) {

            if(po.getStatus().getContainerStatuses() == null) {
                List<ContainerStatusesItem> list = new ArrayList<>();
                ContainerStatusesItem item = new ContainerStatusesItem();
                item.setRestartCount(0);

                list.add(item);

                po.getStatus().setContainerStatuses(list);
            }
        }

        return commonService.setResultModel(podsListAdminList, Constants.RESULT_STATUS_SUCCESS);
    }


}
