package org.paasta.container.platform.api.endpoints;

import org.paasta.container.platform.api.clusters.nodes.NodesAdmin;
import org.paasta.container.platform.api.clusters.nodes.NodesService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.CommonCondition;
import org.paasta.container.platform.api.endpoints.support.EndPointsDetailsItemAdmin;
import org.paasta.container.platform.api.endpoints.support.EndpointAddress;
import org.paasta.container.platform.api.endpoints.support.EndpointPort;
import org.paasta.container.platform.api.endpoints.support.EndpointSubset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the endpoints list
     */
    EndpointsList getEndpointsList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {


        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);


        EndpointsList endpointsList = commonService.setResultObject(responseMap, EndpointsList.class);
        endpointsList = commonService.resourceListProcessing(endpointsList, offset, limit, orderBy, order, searchName, EndpointsList.class);


        return (EndpointsList) commonService.setResultModel(endpointsList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Endpoints 목록 조회(Get Endpoints list)
     * (Admin Portal)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the endpoints list
     */
    public Object getEndpointsListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        EndpointsListAdmin endpointsListAdmin = commonService.setResultObject(responseMap, EndpointsListAdmin.class);
        endpointsListAdmin = commonService.resourceListProcessing(endpointsListAdmin, offset, limit, orderBy, order, searchName, EndpointsListAdmin.class);

        return commonService.setResultModel(endpointsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Endpoints 상세 조회(Get Endpoints list)
     * (User Portal)
     *
     * @param namespace     the namespace
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
     * @param namespace     the namespace
     * @param endpointsName the endpoints name
     * @return the endpoints detail
     */
    public Object getEndpointsAdmin(String namespace, String endpointsName) {

        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsGetUrl()
                        .replace("{namespace}", namespace).replace("{name}", endpointsName)
                , HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        EndpointsAdmin endpointsAdmin = commonService.setResultObject(responseMap, EndpointsAdmin.class);
        endpointsAdmin = endpointsAdminProcessing(endpointsAdmin);

        return commonService.setResultModel(endpointsAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Endpoints YAML 조회(Get Endpoints yaml)
     *
     * @param namespace     the namespace
     * @param endpointsName the endpoints name
     * @param resultMap     the result map
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


    /**
     * Node 명에 따른 Node "Ready" 상태 값 조회 (Get Node "Ready" Status Value by Node Name)
     *
     * @param endpointsAdmin the endpoints admin
     * @return the endpointsAdmin
     */
    public EndpointsAdmin endpointsAdminProcessing(EndpointsAdmin endpointsAdmin) {

        List<EndPointsDetailsItemAdmin> endPointsDetailsItemAdminsList = new ArrayList<>();

        List<EndpointSubset> susbsets = endpointsAdmin.getSubsets();

        for (EndpointSubset es : susbsets) {

            List<EndpointAddress> addresses = es.getAddresses();
            List<EndpointPort> ports = es.getPorts();

            if (addresses == null) {
                addresses = es.getNotReadyAddresses();
            }

            if (ports != null) {

                for (EndpointAddress endpointAddress : addresses) {

                    EndPointsDetailsItemAdmin endPointsDetailsItem = new EndPointsDetailsItemAdmin();
                    String nodeName = CommonUtils.resourceNameCheck(endpointAddress.getNodeName());

                    for (EndpointPort endpointPort : ports) {
                        endpointPort.setName(CommonUtils.resourceNameCheck(endpointPort.getName()));
                    }

                    String nodeReady = Constants.noName;

                    if (!nodeName.equals(Constants.noName)) {
                        NodesAdmin nodesDetails = (NodesAdmin) nodesService.getNodesAdmin(nodeName);

                        List<CommonCondition> nodeConditionList = nodesDetails.getStatus().getConditions();

                        for (CommonCondition condition : nodeConditionList) {
                            if (condition.getType().equals("Ready")) {
                                nodeReady = condition.getStatus();
                            }
                        }
                    }

                    endPointsDetailsItem.setHost(endpointAddress.getIp());
                    endPointsDetailsItem.setPorts(ports);
                    endPointsDetailsItem.setNodes(nodeName);
                    endPointsDetailsItem.setReady(nodeReady);

                    endPointsDetailsItemAdminsList.add(endPointsDetailsItem);

                }
            }

        }

        EndpointsAdmin returnEndpointsAdmin = new EndpointsAdmin();
        returnEndpointsAdmin.setEndpoints(endPointsDetailsItemAdminsList);

        return returnEndpointsAdmin;
    }


    /**
     * 전체 Namespaces 의 Endpoints Admin 목록 조회(Get Endpoints Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the services admin list
     */
    public Object getEndPointsListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        EndpointsListAdmin endpointsListAdmin = commonService.setResultObject(responseMap, EndpointsListAdmin.class);
        endpointsListAdmin = commonService.resourceListProcessing(endpointsListAdmin, offset, limit, orderBy, order, searchName, EndpointsListAdmin.class);

        return commonService.setResultModel(endpointsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

}
