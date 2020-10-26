package org.paasta.container.platform.api.clusters.nodes;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Nodes Service 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.14
 */
@Service
public class NodesService {
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Nodes service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public NodesService(RestTemplateService restTemplateService, CommonService commonService,
                        PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * Nodes 목록 조회(Get Nodes list)
     *
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the nodes list
     */
    NodesList getNodesList(int limit, String continueToken) {
        String param = "";
        if(continueToken != null){
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNodesListUrl() + "?limit" + limit + param,
                    HttpMethod.GET, null, Map.class);

        return (NodesList) commonService.setResultModel(commonService.setResultObject(responseMap, NodesList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Nodes Admin 목록 조회(Get Nodes Admin list)
     *
     * @param limit the limit
     * @param continueToken the continueToken
     * @param searchParam the search param
     * @return the nodes admin list
     */
    public Object getNodesListAdmin(int limit, String continueToken, String searchParam) {
        String param = "";
        HashMap responseMap = null;

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        Object response = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNodesListUrl() + "?limit" + limit + param,
                HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, NodesListAdmin.class), Constants.RESULT_STATUS_SUCCESS);

    }

    /**
     * Nodes 상세 조회(Get Nodes detail)
     *
     * @param resourceName the resource name
     * @return the nodes
     */
    Nodes getNodes(String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNodesGetUrl()
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (Nodes) commonService.setResultModel(commonService.setResultObject(responseMap, Nodes.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * NodesAdmin 상세 조회(Get Nodes Admin detail)
     *
     * @param resourceName the resource name
     * @return the nodes admin
     */
    public Object getNodesAdmin(String resourceName) {
        Object obj = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNodesGetUrl()
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        HashMap responseMap;

        try{
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, NodesAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Nodes YAML 조회(Get Nodes yaml)
     *
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return the nodes yaml
     */
    public Nodes getNodesYaml(String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNodesGetUrl()
                    .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        resultMap.put("sourceTypeYaml", resultString);

        return (Nodes) commonService.setResultModel(commonService.setResultObject(resultMap, Nodes.class), Constants.RESULT_STATUS_SUCCESS);
    }
}
