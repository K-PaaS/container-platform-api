package org.paasta.container.platform.api.endpoints;

import org.paasta.container.platform.api.clusters.nodes.NodesService;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.CommonAddresses;
import org.paasta.container.platform.api.common.model.CommonSubset;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.workloads.deployments.Deployments;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsAdmin;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsListAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Endpoints Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Service
public class EndpointsService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;
    private final NodesService nodesService;

    /**
     * Instantiates a new Endpoints service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public EndpointsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService, NodesService nodesService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.nodesService = nodesService;
    }


    /**
     * Endpoints 목록 조회(Get Endpoints list)
     * (User Portal)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the endpoints list
     */
    EndpointsList getEndpointsList(String namespace, int limit, String continueToken) {
        String param = "";

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (EndpointsList) commonService.setResultModel(commonService.setResultObject(responseMap, EndpointsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Endpoints 목록 조회(Get Endpoints list)
     * (Admin Portal)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @return the endpoints list
     */
    public Object getEndpointsListAdmin(String namespace, int limit, String continueToken, String searchParam) {
        String param = "";
        HashMap responseMap = null;

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        Object response = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param, HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, EndpointsListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Endpoints 상세 조회(Get Endpoints list)
     * (User Portal)
     *
     * @param namespace the namespace
     * @param endpointsName the endpoints name
     * @return the endpoints list
     */
    Endpoints getEndpoints(String namespace, String endpointsName) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", endpointsName),
                HttpMethod.GET, null, Map.class);

        return (Endpoints) commonService.setResultModel(commonService.setResultObject(resultMap, Endpoints.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Endpoints 상세 조회(Get Endpoints detail)
     * (Admin Portal)
     *
     * @param namespace the namespace
     * @param endpointsName the endpoints name
     * @return the endpoints detail
     */
    public Object getEndpointsAdmin(String namespace, String endpointsName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", endpointsName)
                , HttpMethod.GET, null, Map.class);

        HashMap responseMap;

        try{
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        EndpointsAdmin endpointsAdmin = commonService.setResultObject(responseMap, EndpointsAdmin.class);
        String nodeName = endpointsAdmin.getSubsets().get(0).getAddresses().get(0).getNodeName();

        Object nodeStatus = nodesService.getNodesAdmin(nodeName);

        return commonService.setResultModel(null, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Endpoints YAML 조회(Get Endpoints yaml)
     *
     * @param namespace the namespace
     * @param endpointsName the endpoints name
     * @param resultMap the result map
     * @return the endpoints yaml
     */
    public Endpoints getEndpointsYaml(String namespace, String endpointsName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", endpointsName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (Endpoints) commonService.setResultModel(commonService.setResultObject(resultMap, Endpoints.class), Constants.RESULT_STATUS_SUCCESS);
    }

}
