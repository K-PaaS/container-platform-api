package org.paasta.container.platform.api.workloads.replicaSets;

import org.paasta.container.platform.api.common.*;

import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.customServices.CustomServices;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsAdmin;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsListAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * ReplicaSets Service 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.09.10
 */
@Service
public class ReplicaSetsService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new ReplicaSet service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public ReplicaSetsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets list)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the replicaSets list
     */
    public ReplicaSetsList getReplicaSetsList(String namespace, int limit, String continueToken) {

        String param = "";

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                ,HttpMethod.GET, null, Map.class);

        return (ReplicaSetsList) commonService.setResultModel(commonService.setResultObject(resultMap, ReplicaSetsList.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * ReplicaSets 상세 조회(Get ReplicaSets detail)
     *
     * @param namespace the namespace
     * @param replicaSetsName the replicaSets name
     * @return the replicaSets detail
     */
    public ReplicaSets getReplicaSets(String namespace, String replicaSetsName) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", replicaSetsName)
                , HttpMethod.GET, null, Map.class);

        return (ReplicaSets) commonService.setResultModel(commonService.setResultObject(resultMap, ReplicaSets.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ReplicaSets 상세 조회(Get ReplicaSets detail)
     * (Admin Portal)
     *
     * @param namespace the namespace
     * @param replicaSetsName the replicaSets name
     * @return the deployments detail
     */
    public Object getReplicaSetsAdmin(String namespace, String replicaSetsName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", replicaSetsName)
                , HttpMethod.GET, null, Map.class);
        HashMap responseMap;

        try{
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, ReplicaSetsAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ReplicaSets YAML 조회(Get ReplicaSets yaml)
     *
     * @param namespace the namespace
     * @param replicaSetsName the replicaSets name
     * @return the replicaSets yaml
     */
    public ReplicaSets getReplicaSetsYaml(String namespace, String replicaSetsName) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", replicaSetsName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        HashMap<String,Object> resultMap = new HashMap<>();
        resultMap.put("sourceTypeYaml", resultString);

        return (ReplicaSets) commonService.setResultModel(commonService.setResultObject(resultMap, ReplicaSets.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets Selector)
     *
     * @param namespace the namespace
     * @param selectors the selectors
     * @return the replicaSets list
     */
    public ReplicaSetsList getReplicaSetsListLabelSelector(String namespace, String selectors) {
        String requestSelector = "?labelSelector=" + selectors;
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsListUrl()
                        .replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        return (ReplicaSetsList) commonService.setResultModel(commonService.setResultObject(resultMap, ReplicaSetsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets list)
     *(Admin Portal)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @return the replicaSets list
     */
    public Object ReplicaSetsListAdmin(String namespace, int limit, String continueToken, String searchParam) {
        String param = "";
        HashMap responseMap = null;

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        Object response = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param, HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, ReplicaSetsListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ReplicaSets 생성(Create ReplicaSets)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object createReplicaSets(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return  commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_REPLICA_SETS);
    }

    /**
     * ReplicaSets 삭제(Delete ReplicaSets)
     *
     * @param namespace the namespace
     * @param name the replicaSets name
     * @return return is succeeded
     */
    public ResultStatus deleteReplicaSets(String namespace, String name) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", name), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_REPLICA_SETS);
    }

    /**
     * ReplicaSets 수정(Update ReplicaSets)
     *
     * @param namespace the namespace
     * @param name the replicaSets name
     * @param yaml the yaml
     * @return return is succeeded
     */
    public ResultStatus updateReplicaSets(String namespace, String name, String yaml) {
        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListReplicaSetsUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", name), HttpMethod.PUT, yaml, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_REPLICA_SETS_DETAIL.replace("{replicaSetName:.+}", name));
    }


}
