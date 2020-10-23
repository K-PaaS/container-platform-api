package org.paasta.container.platform.api.endpoints;

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

    /**
     * Instantiates a new Endpoints service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public EndpointsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * Endpoints 목록 조회(Get Endpoints list)
     *
     * @param namespace the namespace
     * @return the endpoints list
     */
    EndpointsList getEndpointsList(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (EndpointsList) commonService.setResultModel(commonService.setResultObject(resultMap, EndpointsList.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Endpoints 상세 조회(Get Endpoints detail)
     *
     * @param namespace   the namespace
     * @param serviceName the service name
     * @return the endpoints detail
     */
    Endpoints getEndpoints(String namespace, String serviceName) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListEndpointsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", serviceName), HttpMethod.GET, null, Map.class);

        return (Endpoints) commonService.setResultModel(commonService.setResultObject(resultMap, Endpoints.class), Constants.RESULT_STATUS_SUCCESS);
    }

}
