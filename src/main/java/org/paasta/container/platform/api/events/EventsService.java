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
     * @param namespace the namespace
     * @param resourceUid the resourceUid
     * @return the events list
     */
    EventsList getEventsList(String namespace, String resourceUid, String type) {
        String requestSelector = "?fieldSelector=involvedObject.uid=" + resourceUid;

        if(type != null) {
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
}
