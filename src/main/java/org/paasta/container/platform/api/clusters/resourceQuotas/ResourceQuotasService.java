package org.paasta.container.platform.api.clusters.resourceQuotas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.YamlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public ResourceQuotasList getResourceQuotasList(String namespace, int limit, String continueToken) {

        String param = "";
        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace) + "?limit" + limit + param,
                HttpMethod.GET, null, Map.class);

        return (ResourceQuotasList) commonService.setResultModel(commonService.setResultObject(responseMap, ResourceQuotasList.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * ResourceQuotas Admin 목록 조회(Get ResourceQuotas Admin list)
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continue token
     * @param searchParam the search param
     * @return the resourceQuotas admin list
     */
    public Object getResourceQuotasListAdmin(String namespace, int limit, String continueToken, String searchParam) {
        String param = "";
        HashMap responseMap = null;

        if (continueToken != null) {
            param = "&continue=" + continueToken;
        }

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param, HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, ResourceQuotasListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
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
     * ResourceQuotasAdmin 상세 조회(Get ResourceQuotas Admin detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the resourceQuotas admin
     */
    public Object getResourceQuotasAdmin(String namespace, String resourceName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        HashMap responseMap;

        try {
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, ResourceQuotasAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas YAML 조회(Get ResourceQuotas yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the resultMap
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
        Map YamlMetadata = YamlUtil.parsingYamlMap(yaml, "metadata");
        String createYamlResourceName = YamlMetadata.get("name").toString();

        // ResourceQuotas DB Template name don't use when common create ResourceQuotas
        if (Constants.DEFAULT_LOW_RESOURCE_QUOTA_NAME.equals(createYamlResourceName)
                || Constants.DEFAULT_MEDIUM_RESOURCE_QUOTA_NAME.equals(createYamlResourceName)
                || Constants.DEFAULT_HIGH_RESOURCE_QUOTA_NAME.equals(createYamlResourceName)) {
            return new ResultStatus(Constants.RESULT_STATUS_FAIL, CommonStatusCode.CONFLICT.getMsg(),
                    CommonStatusCode.CONFLICT.getCode(),CommonStatusCode.CONFLICT.getMsg(), null );
        }

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
     * @return the return is succeeded
     */
    public ResultStatus deleteResourceQuotas(String namespace, String resourceName) {
        ResultStatus resultStatus = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_RESOURCE_QUOTAS);
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
                propertyService.getCpMasterApiListResourceQuotasUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModel(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * ResourceQuotas Default Template 목록 조회(Get ResourceQuotas Default Template list)
     *
     * @param namespace the namespace
     * @return the resourceQuotas list
     * @throws JsonProcessingException
     */
    public Object getRqDefaultList(String namespace) throws JsonProcessingException {
        ResourceQuotasListAdmin resourceQuotasList = (ResourceQuotasListAdmin) getResourceQuotasListAdmin(namespace, 0, null, null);
        ResourceQuotasDefaultList resourceQuotasDefaultList = restTemplateService.send(Constants.TARGET_COMMON_API, "/resourceQuotas", HttpMethod.GET, null, ResourceQuotasDefaultList.class);

        ResourceQuotasDefaultList defaultList = new ResourceQuotasDefaultList();
        ResourceQuotasDefault quotasDefault;
        List<ResourceQuotasDefault> quotasDefaultList = new ArrayList<>();

        for(ResourceQuotasDefault resourceQuotasDefault:resourceQuotasDefaultList.getItems()) {
            quotasDefaultList.add(resourceQuotasDefault);
        }

        if(resourceQuotasList.getItems().size() > 0) {
            for (ResourceQuotasListAdminItem i:resourceQuotasList.getItems()) {
                ObjectMapper mapper = new ObjectMapper();
                String status = mapper.writeValueAsString(i.getStatus());

                quotasDefault = new ResourceQuotasDefault(i.getName(), status);
                quotasDefaultList.add(quotasDefault);

            }

            defaultList.setItems(quotasDefaultList);

            return commonService.setResultModel(defaultList, Constants.RESULT_STATUS_SUCCESS);
        }

        defaultList.setItems(quotasDefaultList);
        return commonService.setResultModel(defaultList, Constants.RESULT_STATUS_SUCCESS);
    }
}
