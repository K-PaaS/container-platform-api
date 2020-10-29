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
     * Events 목록 조회(Get Events list)
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
     * @param namespace     the namespace
     * @param limit         the limit
     * @param continueToken the continueToken
     * @param searchParam the searchParam
     * @return the events list
     */
    public Object getEventsListAdmin(String namespace, int limit, String continueToken, String searchParam) {
        String param = "";
        HashMap responseMap = null;

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        Object response = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param, HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, EventsListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Events 목록 조회(Get Events list)
     * (User Portal)
     *
     * @param namespace     the namespace
     * @param limit         the limit
     * @param continueToken the continueToken
     * @return the events list
     */
    public EventsList getEventsList(String namespace, int limit, String continueToken) {
        String param = "";

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (EventsList) commonService.setResultModel(commonService.setResultObject(responseMap, EventsList.class), Constants.RESULT_STATUS_SUCCESS);
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

}