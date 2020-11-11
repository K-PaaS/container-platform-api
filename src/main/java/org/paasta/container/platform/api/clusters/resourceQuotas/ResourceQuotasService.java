package org.paasta.container.platform.api.clusters.resourceQuotas;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.CommonMetaData;
import org.paasta.container.platform.api.common.model.CommonResourcesYaml;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.common.util.YamlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.paasta.container.platform.api.common.Constants.CHECK_N;
import static org.paasta.container.platform.api.common.Constants.CHECK_Y;

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
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the resourceQuotas list
     */
    public ResourceQuotasList getResourceQuotasList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace),
                HttpMethod.GET, null, Map.class);

        ResourceQuotasList resourceQuotasList = commonService.setResultObject(responseMap, ResourceQuotasList.class);
        resourceQuotasList = commonService.resourceListProcessing(resourceQuotasList, offset, limit, orderBy, order, searchName, ResourceQuotasList.class);

        return (ResourceQuotasList) commonService.setResultModel(resourceQuotasList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * ResourceQuotas Admin 목록 조회(Get ResourceQuotas Admin list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the resourceQuotas admin list
     */
    public Object getResourceQuotasListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        ResourceQuotasListAdmin resourceQuotasListAdmin = commonService.setResultObject(responseMap, ResourceQuotasListAdmin.class);
        resourceQuotasListAdmin = commonService.resourceListProcessing(resourceQuotasListAdmin, offset, limit, orderBy, order, searchName, ResourceQuotasListAdmin.class);

        return commonService.setResultModel(resourceQuotasListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 상세 조회(Get ResourceQuotas detail)
     *
     * @param namespace    the namespace
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
     * @param namespace    the namespace
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
     * ResourceQuotas Admin YAML 조회(Get ResourceQuotas Admin yaml)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param resultMap    the resultMap
     * @return the resourceQuotas yaml
     */
    public Object getResourceQuotasAdminYaml(String namespace, String resourceName, HashMap resultMap) {
        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        if (CommonUtils.isResultStatusInstanceCheck(response)) {
            return response;
        }
        //noinspection unchecked
        resultMap.put("sourceTypeYaml", response);

        return  commonService.setResultModel(commonService.setResultObject(resultMap, CommonResourcesYaml.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 생성(Create ResourceQuotas)
     *
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    public Object createResourceQuotas(String namespace, String yaml, boolean isAdmin) {
        Map YamlMetadata = YamlUtil.parsingYamlMap(yaml, "metadata");
        String createYamlResourceName = YamlMetadata.get("name").toString();

        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class, isAdmin);

        return commonService.setResultModel(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * ResourceQuotas 삭제(Delete ResourceQuotas)
     *
     * @param namespace    the namespace
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
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    public Object updateResourceQuotas(String namespace, String resourceName, String yaml) {
        Object resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class, true);

        return commonService.setResultModel(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * ResourceQuotas Default Template 목록 조회(Get ResourceQuotas Default Template list)
     *
     * @param namespace the namespace
     * @return the resourceQuotas list
     * @throws JsonProcessingException
     */
    public Object getRqDefaultList(String namespace, int offset, int limit, String orderBy, String order, String searchName) throws JsonProcessingException {
        ResourceQuotasListAdmin resourceQuotasList = (ResourceQuotasListAdmin) getResourceQuotasListAdmin(namespace, 0, 0, "creationTime", "desc", "");
        ResourceQuotasDefaultList resourceQuotasDefaultList = restTemplateService.send(Constants.TARGET_COMMON_API, "/resourceQuotas", HttpMethod.GET, null, ResourceQuotasDefaultList.class);

        ResourceQuotasDefaultList defaultList = new ResourceQuotasDefaultList();
        ResourceQuotasDefault quotasDefault;
        List<ResourceQuotasDefault> quotasDefaultList = new ArrayList<>();

        List<String> k8sRqNameList = resourceQuotasList.getItems().stream().map(ResourceQuotasListAdminItem::getName).collect(Collectors.toList());
        List<String> dbRqNameList = resourceQuotasDefaultList.getItems().stream().map(ResourceQuotasDefault::getName).collect(Collectors.toList());

        for(ResourceQuotasDefault resourceQuotasDefault : resourceQuotasDefaultList.getItems()) {
            String yn = CHECK_N;

            if (k8sRqNameList.contains(resourceQuotasDefault.getName())) {
                yn = CHECK_Y;
            }

            CommonMetaData metadata = new CommonMetaData();

            metadata.setName(resourceQuotasDefault.getName());
            metadata.setCreationTimestamp(resourceQuotasDefault.getCreationTimestamp());

            resourceQuotasDefault.setCheckYn(yn);
            resourceQuotasDefault.setMetadata(metadata);
            quotasDefaultList.add(resourceQuotasDefault);
        }

        if (resourceQuotasList.getItems().size() > 0) {
            for (ResourceQuotasListAdminItem i : resourceQuotasList.getItems()) {
                ObjectMapper mapper = new ObjectMapper();
                String status = mapper.writeValueAsString(i.getStatus());

                if (!dbRqNameList.contains(i.getName())) {
                    quotasDefault = new ResourceQuotasDefault(i.getName(), status, CHECK_Y, i.getMetadata(), i.getCreationTimestamp());
                    quotasDefaultList.add(quotasDefault);
                }

            }

            defaultList.setItems(quotasDefaultList);
            defaultList = commonService.setResultObject(defaultList, ResourceQuotasDefaultList.class);
            defaultList = commonService.resourceListProcessing(defaultList, offset, limit, orderBy, order, searchName, ResourceQuotasDefaultList.class);

            return commonService.setResultModel(defaultList, Constants.RESULT_STATUS_SUCCESS);
        }

        defaultList.setItems(quotasDefaultList);
        defaultList = commonService.setResultObject(defaultList, ResourceQuotasDefaultList.class);
        defaultList = commonService.resourceListProcessing(defaultList, offset, limit, orderBy, order, searchName, ResourceQuotasDefaultList.class);

        return commonService.setResultModel(defaultList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 전체 Namespaces 의 ResourceQuotas Admin 목록 조회(Get ResourceQuotas Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the resourceQuotas all list
     */
    public Object getResourceQuotasListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListAllNamespacesUrl() + commonService.generateFieldSelectorForExceptNamespace()
                , HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        ResourceQuotasListAdmin resourceQuotasListAdmin = commonService.setResultObject(responseMap, ResourceQuotasListAdmin.class);
        resourceQuotasListAdmin = commonService.resourceListProcessing(resourceQuotasListAdmin, offset, limit, orderBy, order, searchName, ResourceQuotasListAdmin.class);

        return commonService.setResultModel(resourceQuotasListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }
}
