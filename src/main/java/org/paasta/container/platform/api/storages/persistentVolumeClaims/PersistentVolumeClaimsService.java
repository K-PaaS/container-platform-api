package org.paasta.container.platform.api.storages.persistentVolumeClaims;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.customServices.CustomServices;
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
     * Instantiates a new deployments service.
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
     * PersistentVolumeClaims 목록을 조회한다.
     *
     * @param namespace the namespace
     * @param limit the limit
     * @param continueToken the continueToken
     * @return the PersistentVolumeClaims List
     */
    public PersistentVolumeClaimsList getPersistentVolumeClaimsList(String namespace,int limit, String continueToken) {

        String param = "";

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentvolumeclaimsListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (PersistentVolumeClaimsList) commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumeClaimsList.class), Constants.RESULT_STATUS_SUCCESS);

    }


    /**
     * PersistentVolumeClaims 상세 정보를 조회한다.
     *
     * @param namespace                     the namespace
     * @param resourceName  the PersistentVolumeClaim name
     * @return the PersistentVolumeClaims
     */
    public PersistentVolumeClaims getPersistentVolumeClaims(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentvolumeclaimsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        return (PersistentVolumeClaims) commonService.setResultModel(commonService.setResultObject(responseMap, PersistentVolumeClaims.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * PersistentVolumeClaims YAML 을 조회한다.
     *
     * @param namespace                    the namespace
     * @param resourceName the PersistentVolumeClaim name
     * @param resultMap                    the result map
     * @return the PersistentVolumeClaims
     */
    public PersistentVolumeClaims getPersistentVolumeClaimsYaml(String namespace, String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentvolumeclaimsGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        //noinspection unchecked
        resultMap.put("sourceTypeYaml", resultString);

        return (PersistentVolumeClaims) commonService.setResultModel(commonService.setResultObject(resultMap, PersistentVolumeClaims.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * PersistentVolumeClaims 를 생성한다.
     *
     * @param namespace       the namespace
     * @param yaml            the yaml
     * @return                 return is succeeded
     */
    public Object createPersistentVolumeClaims(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentvolumeclaimsCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return  commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * PersistentVolumeClaims를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the PersistentVolumeClaim name
     * @param resultMap the result map
     * @return the ResultStatus
     */
    public ResultStatus deletePersistentVolumeClaims(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentvolumeclaimsDeleteUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES);
    }

    /**
     * PersistentVolumeClaims를  수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the PersistentVolumeClaim name
     * @param yaml          the yaml
     * @return the services
     */

    public Object updatePersistentVolumeClaims(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListPersistentvolumeclaimsUpdateUrl()
                        .replace("{namespace}", namespace).replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, CustomServices.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_STORAGES_DETAIL.replace("{persistentVolumeClaimName:.+}", resourceName));
    }
}
