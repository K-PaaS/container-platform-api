package org.paasta.container.platform.api.managements.resourceQuotas;

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
 * ResourceQuotas Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.03
 **/
@Service
public class ResourceQuotasService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Namespace service
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public ResourceQuotasService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * ResourceQuota 목록을 조회한다.
     *
     * @param namespace the namespaces
     * @return ResourceQuotaList the ResourceQuotaList
     */
    ResourceQuotasList getResourceQuotasList(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (ResourceQuotasList) commonService.setResultModel(commonService.setResultObject(resultMap, ResourceQuotasList.class), Constants.RESULT_STATUS_SUCCESS);
    }
}
