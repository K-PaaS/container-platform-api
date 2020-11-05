package org.paasta.container.platform.api.events;

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
 * Events Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.11.05
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
     * @param type        the type
     * @return the events list
     */
    EventsList getEventsList(String namespace, String resourceUid, String type, int offset, int limit, String orderBy, String order, String searchName) {

        String requestSelector = generateFieldSelector(resourceUid);
        HashMap resultMap = null;

        if (type != null) {
            //node
            requestSelector = "?fieldSelector=involvedObject.name=" + resourceUid;

            resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListEventsListAllNamespacesUrl() + requestSelector, HttpMethod.GET, null, Map.class);
        } else {

            resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListEventsListUrl()
                            .replace("{namespace}", namespace) + requestSelector, HttpMethod.GET, null, Map.class);

        }

        EventsList eventsList = commonService.setResultObject(resultMap, EventsList.class);
        eventsList = commonService.resourceListProcessing(eventsList, offset, limit, orderBy, order, searchName, EventsList.class);

        return (EventsList) commonService.setResultModel(eventsList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 특정 Namespace 의 전체 Events 목록 조회(Get Events list in a Namespace)
     *
     * @param namespace the namespace
     * @return the events list
     */
    EventsList getNamespaceEventsList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        EventsList eventsList = commonService.setResultObject(resultMap, EventsList.class);
        eventsList = commonService.resourceListProcessing(eventsList, offset, limit, orderBy, order, searchName, EventsList.class);

        return (EventsList) commonService.setResultModel(eventsList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Events Admin 목록 조회(Get Events Admin list)
     *
     * @param namespace   the namespace
     * @param resourceUid the resourceUid
     * @param offset      the offset
     * @param limit       the limit
     * @param orderBy     the orderBy
     * @param order       the order
     * @param searchName  the searchName
     * @return the events list
     */
    public Object getEventsListAdmin(String namespace, String resourceUid, String type, int offset, int limit, String orderBy, String order, String searchName) {
        Object response = null;
        HashMap responseMap = null;

        String param = generateFieldSelector(resourceUid);

        if (type != null) {
            //node
            param = "?fieldSelector=involvedObject.name=" + resourceUid;

            response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListEventsListAllNamespacesUrl() + param, HttpMethod.GET, null, Map.class);
        } else {

            response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                    propertyService.getCpMasterApiListEventsListUrl()
                            .replace("{namespace}", namespace) + param, HttpMethod.GET, null, Map.class);

        }

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
     * 특정 Namespace 의 전체 Events Admin 목록 조회(Get Events Admin list in a Namespace)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the events list
     */
    public Object getNamespaceEventsListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEventsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

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
     * Field Selector Parameter 생성 ( Generate Field Selector Parameter )
     *
     * @param resourceUid the namespace
     * @return the string
     */
    public String generateFieldSelector(String resourceUid) {
        return "?fieldSelector=involvedObject.uid=" + resourceUid;
    }


}
