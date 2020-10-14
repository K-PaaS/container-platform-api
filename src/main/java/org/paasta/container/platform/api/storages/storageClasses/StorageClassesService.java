package org.paasta.container.platform.api.storages.storageClasses;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.customServices.CustomServices;
import org.paasta.container.platform.api.storages.persistentVolumeClaims.PersistentVolumeClaims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

/**
 * StorageClasses Service 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.13
 */
@Service
public class StorageClassesService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new deployments service.
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public StorageClassesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }

    /**
     * StorageClasses 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the StorageClasses List
     */
    public StorageClassesList getStorageClassesList(String namespace) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListServicesListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);

        return (StorageClassesList) commonService.setResultModel(commonService.setResultObject(responseMap, StorageClassesList.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * StorageClasses 상세 정보를 조회한다.
     *
     * @param namespace    the namespace
     * @param resourceName the StorageClasses name
     * @return the StorageClasses
     */
    public StorageClasses getStorageClasses(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListStorageclassesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (StorageClasses) commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumeClaims.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * StorageClasses YAML 을 조회한다.
     *
     * @param namespace                    the namespace
     * @param resourceName the StorageClasses name
     * @param resultMap                    the result map
     * @return the StorageClasses
     */
    public StorageClasses getStorageClassesYaml(String namespace, String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListStorageclassesGetUrl()
                    .replace("{namespace}", namespace)
                    .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        resultMap.put("sourceTypYaml", resultString);

        return (StorageClasses) commonService.setResultModel(commonService.setResultObject(resultMap, StorageClasses.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * StorageClasses 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return                 return is succeeded
     */
    public Object createStorageClasses(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListStorageclassesCreate()
                    .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * StorageClasses 를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the StorageClasses name
     * @param resultMap the result map
     * @return the ResultStatus
     */
    public ResultStatus deleteStorageClasses(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListStorageclassesDelete()
                    .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * StorageClasses 를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the StorageClasses name
     * @param yaml          the yaml
     * @return the services
     */
    public Object updateStorageClasses(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListStorageclassesUpdate()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);
        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, CustomServices.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }
}
