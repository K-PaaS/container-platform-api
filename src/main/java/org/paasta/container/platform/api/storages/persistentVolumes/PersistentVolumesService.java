package org.paasta.container.platform.api.storages.persistentVolumes;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.customServices.CustomServices;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsAdmin;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsList;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsListAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * PersistentVolumes Service 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.19
 */
@Service
public class PersistentVolumesService {
    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new PersistentVolumes service
     *
     * @param restTemplateService the rest template service
     * @param commonService        the common service
     * @param propertyService      the property service
     */
    @Autowired
    public PersistentVolumesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * PersistentVolumes 목록 조회(Get PersistentVolumes list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     */
    public PersistentVolumesList getPersistentVolumesList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesListUrl()
                , HttpMethod.GET, null, Map.class);

        PersistentVolumesList persistentVolumesList = commonService.setResultObject(responseMap, PersistentVolumesList.class);
        persistentVolumesList = commonService.resourceListProcessing(persistentVolumesList, offset, limit, orderBy, order, searchName, PersistentVolumesList.class);

        return (PersistentVolumesList) commonService.setResultModel(persistentVolumesList, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumes 목록 조회(Get PersistentVolumes list)
     *(Admin Portal)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the persistentVolumes list
     */
    public Object getPersistentVolumesListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        String param = "";
        HashMap responseMap = null;

        Object response = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        PersistentVolumesListAdmin persistentVolumesListAdmin = commonService.setResultObject(responseMap, PersistentVolumesListAdmin.class);
        persistentVolumesListAdmin = commonService.resourceListProcessing(persistentVolumesListAdmin, offset, limit, orderBy, order, searchName, PersistentVolumesListAdmin.class);


        return commonService.setResultModel(persistentVolumesListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumes 상세 조회(Get PersistentVolumes detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the persistentVolumes detail
     */
    public PersistentVolumes getPersistentVolumes(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (PersistentVolumes) commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumes.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumes YAML 조회(Get PersistentVolumes yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return the persistentVolumes yaml
     */
    public PersistentVolumes getPersistentVolumesYaml(String namespace, String resourceName, HashMap resultMap) {
        String resulString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resulString);

        return (PersistentVolumes) commonService.setResultModel(commonService.setResultObject(resultMap, PersistentVolumes.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumes 생성(Create PersistentVolumes)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object createPersistentVolumes(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * PersistentVolumes 삭제(Delete PersistentVolumes)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return return is succeeded
     */
    public ResultStatus deletePersistentVolumes(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesDeleteUrl()
                        .replace("{namesapce}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * PersistentVolumes 수정(Update PersistentVolumes)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object updatePersistentVolumes(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, PersistentVolumes.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES_DETAIL.replace("{persistentVolumeName:.+}", resourceName));
    }

    /**
     * PersistentVolumes 상세 조회(Get PersistentVolumes detail)
     * (Admin Portal)
     *
     * @param namespace the namespace
     * @param persistentVolumesName the persistentVolumes name
     * @return the persistentVolumes detail
     */
    public Object getPersistentVolumesAdmin(String namespace, String persistentVolumesName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", persistentVolumesName)
                , HttpMethod.GET, null, Map.class);
        HashMap responseMap;

        try {
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumesAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 전체 Namespaces 의 PersistentVolumes Admin 목록 조회(Get PersistentVolumes Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the persistentVolumes all list
     */
    public Object getPersistentVolumesListAllNamesapcesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumesListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        PersistentVolumesListAdmin persistentVolumesListAdmin = commonService.setResultObject(responseMap, PersistentVolumesListAdmin.class);
        persistentVolumesListAdmin = commonService.resourceListProcessing(persistentVolumesListAdmin, offset, limit, orderBy, order, searchName, PersistentVolumesListAdmin.class);

        return commonService.setResultModel(persistentVolumesListAdmin, Constants.RESULT_STATUS_SUCCESS);

    }
}
