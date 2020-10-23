package org.paasta.container.platform.api.managements.resourceQuotas;

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
     * Instantiates a new ResourceQuotas service
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
     * ResourceQuotas 목록 조회(Get ResourceQuotas list)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the resourceQuotas list
     */
    public ResourceQuotasList getResourceQuotasList(String namespace,  int limit, String continueToken) {

        String param = "";
        if(continueToken != null){
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace) + "?limit" + limit + param,
                HttpMethod.GET, null, Map.class);

        return (ResourceQuotasList) commonService.setResultModel(commonService.setResultObject(responseMap, ResourceQuotasList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 상세 조회(Get ResourceQuotas detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the resourceQuotas detail
     */
    public ResourceQuotas getResourceQuotas(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (ResourceQuotas) commonService.setResultModel(commonService.setResultObject(responseMap, ResourceQuotas.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas YAML 조회(Get ResourceQuotas yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the resourceQuotas yaml
     */
    public ResourceQuotas getResourceQuotasYaml(String namespace, String resourceName, HashMap resultMap) {
        String resulString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resulString);

        return (ResourceQuotas) commonService.setResultModel(commonService.setResultObject(resultMap, ResourceQuotas.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 생성(Create ResourceQuotas)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object createResourceQuotas(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModel(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 삭제(Delete ResourceQuotas)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return the return is succeeded
     */
    public ResultStatus deleteResourceQuotas(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModel(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 수정(Update ResourceQuotas)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object updateResourceQuotas(String namespace, String resourceName, String yaml) {
        Object resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModel(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS);
    }
}
