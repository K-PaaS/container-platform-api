package org.paasta.container.platform.api.customServices;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Custom Services Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.10
 */
@Service
public class CustomServicesService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Custom services service.
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public CustomServicesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * Services 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the custom services list
     */
    public CustomServicesList getCustomServicesList(String namespace, int limit, String continueToken) {
        String param = "";

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (CustomServicesList) commonService.setResultModel(commonService.setResultObject(responseMap, CustomServicesList.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Services 상세 정보를 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the service name
     * @return the custom services
     */
    public CustomServices getCustomServices(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (CustomServices) commonService.setResultModel(commonService.setResultObject(responseMap, CustomServices.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Services YAML을 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the service name
     * @param resultMap   the result map
     * @return the custom services yaml
     */
    public CustomServices getCustomServicesYaml(String namespace, String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (CustomServices) commonService.setResultModel(commonService.setResultObject(resultMap, CustomServices.class), Constants.RESULT_STATUS_SUCCESS);
    }



    /**
     * Services를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return                 return is succeeded
     */
    public Object createServices(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesCreate()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return  commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES);
    }




    /**
     * Services를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the service name
     * @param resultMap the result map
     * @return the ResultStatus
     */
    public ResultStatus deleteServices(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesDelete()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES);
    }


    /**
     * Services를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the service name
     * @param yaml          the yaml
     * @return the services
     */
    public Object updateServices(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesUpdate()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, CustomServices.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES_DETAIL.replace("{serviceName:.+}", resourceName));
    }


}
