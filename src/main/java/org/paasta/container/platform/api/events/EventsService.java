package org.paasta.container.platform.api.events;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.endpoints.Endpoints;
import org.paasta.container.platform.api.endpoints.EndpointsAdmin;
import org.paasta.container.platform.api.endpoints.EndpointsList;
import org.paasta.container.platform.api.endpoints.EndpointsListAdmin;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsList;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsListAdmin;
import org.paasta.container.platform.api.workloads.pods.PodsListAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Events Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.17
 */
@Service
public class EventsService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Events service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public EventsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * Events resourceUid 목록 조회(Get Events resource uid list)
     *
     * @param namespace   the namespace
     * @param resourceUid the resourceUid
     * @param type the type
     * @return the events list
     */
    EventsList getEventsUidList(String namespace, String resourceUid, String type) {
        String requestSelector = "?fieldSelector=involvedObject.uid=" + resourceUid;

        if (type != null) {
            requestSelector = "?fieldSelector=involvedObject.name=" + resourceUid;
        }

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        return (EventsList) commonService.setResultModel(
                commonService.setResultObject(resultMap, EventsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events resourceUid 목록 조회(Get Events resource uid list)
     * (Admin portal)
     *
     * @param namespace   the namespace
     * @param resourceUid the resourceUid
     * @param type the type
     * @return the events list
     */
    public EventsListAdmin getEventsUidListAdmin(String namespace, String resourceUid, String type) {
        String requestSelector = "?fieldSelector=involvedObject.uid=" + resourceUid;

        if (type != null) {
            requestSelector = "?fieldSelector=involvedObject.name=" + resourceUid;
        }

        HashMap resultMap = (HashMap) restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        return (EventsListAdmin) commonService.setResultModel(
                commonService.setResultObject(resultMap, EventsListAdmin.class), Constants.RESULT_STATUS_SUCCESS);

    }

    /**
     * Events resourceName 목록 조회(Get Events resourceName list)
     * (Admin portal)
     *
     * @param namespace   the namespace
     * @param resourceName the resource name
     * @return the events list
     */
    EventsListAdmin getEventsResourceNameListAdmin(String namespace, String resourceName) {
        String requestSelector = "?fieldSelector=involvedObject.name=" + resourceName;

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);





        return (EventsListAdmin) commonService.setResultModel(
                commonService.setResultObject(resultMap, EventsListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events resourceName 목록 조회(Get Events resourceName list)
     * (User portal)
     *
     * @param namespace   the namespace
     * @param resourceName the resource name
     * @return the events list
     */
    EventsList getEventsResourceNameList(String namespace, String resourceName) {
        String requestSelector = "?fieldSelector=involvedObject.name=" + resourceName;

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        return (EventsList) commonService.setResultModel(
                commonService.setResultObject(resultMap, EventsList.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Events 목록 조회(Get Events namespace)
     *
     * @param namespace the namespace
     * @return the events list
     */
    EventsList getNamespaceEventsList(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (EventsList) commonService.setResultModel(
                commonService.setResultObject(resultMap, EventsList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events 목록 조회(Get Events list)
     * (Admin Portal)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the events list
     */
    public Object getEventsListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) , HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        EventsListAdmin eventsListAdmin = commonService.setResultObject(responseMap, EventsListAdmin.class);
        eventsListAdmin = commonService.resourceListProcessing(eventsListAdmin, offset, limit, orderBy, order, searchName, EventsListAdmin.class);

        return commonService.setResultModel(eventsListAdmin,Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events 목록 조회(Get Events list)
     * (User Portal)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the events list
     */
    public EventsList getEventsList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);

        EventsList eventsList = commonService.setResultObject(responseMap, EventsList.class);
        eventsList = commonService.resourceListProcessing(eventsList, offset, limit, orderBy, order, searchName, EventsList.class);

        return (EventsList) commonService.setResultModel(eventsList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Events 상세 조회(Get Events detail)
     * (Admin Portal)
     *
     * @param namespace  the namespace
     * @param eventsName the events name
     * @return the events detail
     */
    public Object getEventsAdmin(String namespace, String eventsName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", eventsName)
                , HttpMethod.GET, null, Map.class);
        HashMap responseMap;

        try {
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, EventsAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events 상세 조회(Get Events list)
     * (User Portal)
     *
     * @param namespace  the namespace
     * @param eventsName the events name
     * @return the events list
     */
    public Events getEvents(String namespace, String eventsName) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", eventsName),
                HttpMethod.GET, null, Map.class);

        return (Events) commonService.setResultModel(commonService.setResultObject(resultMap, Events.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events YAML 조회(Get Events yaml)
     *
     * @param namespace  the namespace
     * @param eventsName the events name
     * @param resultMap  the result map
     * @return the events yaml
     */
    public Events getEventsYaml(String namespace, String eventsName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", eventsName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (Events) commonService.setResultModel(commonService.setResultObject(resultMap, Events.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 전체 Namespaces 의 Events Admin 목록 조회(Get Events Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the events all list
     */
    public Object getEventsListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListAllNamespacesUrl(),  HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        EventsListAdmin eventsListAdmin = commonService.setResultObject(responseMap, EventsListAdmin.class);
        eventsListAdmin = commonService.resourceListProcessing(eventsListAdmin, offset, limit, orderBy, order, searchName, EventsListAdmin.class);

        return commonService.setResultModel(eventsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events 목록 조회(Get Events node)
     *
     * @param namespace the namespace
     * @param nodeName  the node name
     * @return the events list
     */
    public EventsList getEventsListByNode(String namespace, String nodeName) {
        String requestURL = propertyService.getCpMasterApiListEventsListUrl().replace("{namespace}", namespace)
                + "/?fieldSelector=deprecatedSource.node=" + nodeName;

        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API, requestURL,
                HttpMethod.GET, null, Map.class);

        EventsList eventsList = commonService.setResultObject(resultMap, EventsList.class);
        eventsList = commonService.setCommonItemMetaDataBySelector(eventsList, EventsList.class);

        return (EventsList) commonService.setResultModel(eventsList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events 목록 조회(Get Events node)
     * (Admin portal)
     *
     * @param namespace the namespace
     * @param nodeName  the node name
     * @return the events list
     */
    public EventsListAdmin getEventsListByNodeAdmin(String namespace, String nodeName) {
        String requestURL = propertyService.getCpMasterApiListEventsListUrl().replace("{namespace}", namespace)
                + "/?fieldSelector=deprecatedSource.node=" + nodeName;

        HashMap resultMap = (HashMap) restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, requestURL,
                HttpMethod.GET, null, Map.class);

        EventsListAdmin eventsListAdmin = commonService.setResultObject(resultMap, EventsListAdmin.class);
        eventsListAdmin = commonService.setCommonItemMetaDataBySelector(eventsListAdmin, EventsListAdmin.class);

        return (EventsListAdmin) commonService.setResultModel(eventsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


}