package org.paasta.container.platform.api.privateRegistry;

import org.paasta.container.platform.api.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;


/**
 * Private Registry Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.12.01
 */
@Service
public class PrivateRegistryService {
    private final RestTemplateService restTemplateService;
    private final PropertyService propertyService;
    private final CommonService commonService;

    /**
     * Instantiates a new PrivateRegistry service
     *
     * @param restTemplateService the rest template service
     * @param propertyService     the property service
     * @param commonService       the common service
     */
    @Autowired
    public PrivateRegistryService(RestTemplateService restTemplateService, PropertyService propertyService, CommonService commonService) {
        this.restTemplateService = restTemplateService;
        this.propertyService = propertyService;
        this.commonService = commonService;
    }

    /**
     * Private Registry 상세 조회(Get Private Registry detail)
     *
     * @param cluster        the cluster
     * @param namespace      the namespace
     * @param repositoryName the repositoryName
     * @return the private registry
     */
    public Object getPrivateRegistry(String cluster, String namespace, String repositoryName) {

        Object response = restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_PRIVATE_REGISTRY
                .replace("{cluster:.+}", cluster)
                .replace("{repositoryName:.+}", repositoryName), HttpMethod.GET, null, PrivateRegistry.class);

        if (CommonUtils.isResultStatusInstanceCheck(response)) {
            return response;
        }
        return commonService.setResultModel(commonService.setResultObject(response, PrivateRegistry.class), Constants.RESULT_STATUS_SUCCESS);
    }

}
