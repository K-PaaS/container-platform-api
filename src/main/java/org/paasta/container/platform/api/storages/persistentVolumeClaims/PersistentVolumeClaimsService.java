package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.storages.persistentVolumes.PersistentVolumes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * PersistentVolumeClaims Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.18
 */
@Service
public class PersistentVolumeClaimsService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new PersistentVolumeClaims service
     *
     * @param restTemplateService the rest template service
     * @param commonService        the common service
     * @param propertyService      the property service
     */
    @Autowired
    public PersistentVolumeClaimsService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * PersistentVolumeClaims 목록 조회(Get PersistentVolumeClaims list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the persistentVolumeClaims list
     */
    public PersistentVolumeClaimsList getPersistentVolumeClaimsList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);

        PersistentVolumeClaimsList persistentVolumeClaimsList = commonService.setResultObject(responseMap, PersistentVolumeClaimsList.class);
        persistentVolumeClaimsList = commonService.resourceListProcessing(persistentVolumeClaimsList, offset, limit, orderBy, order, searchName, PersistentVolumeClaimsList.class);

        return (PersistentVolumeClaimsList) commonService.setResultModel(persistentVolumeClaimsList, Constants.RESULT_STATUS_SUCCESS);

    }

    /**
     * PersistentVolumeClaims 목록 조회(Get PersistentVolumeClaims list)
     *(Admin Portal)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the persistentVolumeClaims list
     */
    public Object getPersistentVolumeClaimsListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        PersistentVolumeClaimsListAdmin persistentVolumeClaimsListAdmin = commonService.setResultObject(responseMap, PersistentVolumeClaimsListAdmin.class);
        persistentVolumeClaimsListAdmin = commonService.resourceListProcessing(persistentVolumeClaimsListAdmin, offset, limit, orderBy, order, searchName, PersistentVolumeClaimsListAdmin.class);

        return commonService.setResultModel(persistentVolumeClaimsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * PersistentVolumeClaims 상세 조회(Get PersistentVolumeClaims detail)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @return the persistentVolumeClaims detail
     */
    public PersistentVolumeClaims getPersistentVolumeClaims(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (PersistentVolumeClaims) commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumeClaims.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumeClaims 상세 조회(Get PersistentVolumeClaims detail)
     * (Admin Portal)
     *
     * @param namespace the namespace
     * @param persistentVolumeClaimsName the persistentVolumeClaims name
     * @return the persistentVolumeClaims detail
     */
    public Object getPersistentVolumeClaimsAdmin(String namespace, String persistentVolumeClaimsName) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", persistentVolumeClaimsName)
                , HttpMethod.GET, null, Map.class);
        HashMap responseMap;

        try {
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumeClaimsAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumeClaims YAML 조회(Get PersistentVolumeClaims yaml)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return the persistentVolumeClaims yaml
     */
    public PersistentVolumeClaims getPersistentVolumeClaimsYaml(String namespace, String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (PersistentVolumeClaims) commonService.setResultModel(commonService.setResultObject(resultMap, PersistentVolumeClaims.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumeClaims 생성(Create PersistentVolumeClaims)
     *
     * @param namespace the namespace
     * @param yaml the yaml
     * @return return is succeeded
     */
    public Object createPersistentVolumeClaims(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return  commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * PersistentVolumeClaims 삭제(Delete PersistentVolumeClaims)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return return is succeeded
     */
    public ResultStatus deletePersistentVolumeClaims(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * PersistentVolumeClaims 수정(Update PersistentVolumeClaims)
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml the yaml
     * @return return is succeeded
     */

    public Object updatePersistentVolumeClaims(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, PersistentVolumes.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES_DETAIL.replace("{persistentVolumeClaimName:.+}", resourceName));
    }

    /**
     * 전체 Namespaces 의 PersistentVolumeClaims Admin 목록 조회(Get PersistentVolumeClaims Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the persistentVolumeClaims all list
     */
    public Object getPersistentVolumeClaimsListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentVolumeClaimsListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        PersistentVolumeClaimsListAdmin persistentVolumeClaimsListAdmin = commonService.setResultObject(responseMap, PersistentVolumeClaimsListAdmin.class);
        persistentVolumeClaimsListAdmin = commonService.resourceListProcessing(persistentVolumeClaimsListAdmin, offset, limit, orderBy, order, searchName, PersistentVolumeClaimsListAdmin.class);

        return commonService.setResultModel(persistentVolumeClaimsListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }
}
