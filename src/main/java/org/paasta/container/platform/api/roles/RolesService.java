package org.paasta.container.platform.api.roles;

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
 * Roles Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.10.13
 */
@Service
public class RolesService {

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;

    /**
     * Instantiates a new Custom Roles service.
     *
     * @param restTemplateService the rest template service
     * @param commonService       the common service
     * @param propertyService     the property service
     */
    @Autowired
    public RolesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
    }


    /**
     * Roles 목록을 조회한다.
     *
     * @param namespace the namespace
     * @return the roles list
     */
    public RolesList getRolesList(String namespace, int limit, String continueToken) {
        String param = "";

        if(continueToken != null) {
            param = "&continue=" + continueToken;
        }

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesListUrl()
                        .replace("{namespace}", namespace) + "?limit=" + limit + param
                , HttpMethod.GET, null, Map.class);

        return (RolesList) commonService.setResultModel(commonService.setResultObject(responseMap, RolesList.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Roles 상세 정보를 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the resource name
     * @return the roles
     */
    public Roles getRoles(String namespace, String resourceName) {
        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                        , HttpMethod.GET, null, Map.class);

        return (Roles) commonService.setResultModel(commonService.setResultObject(responseMap, Roles.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Roles YAML을 조회한다.
     *
     * @param namespace   the namespace
     * @param resourceName the resource name
     * @param resultMap   the result map
     * @return the roles yaml
     */
    public Roles getRolesYaml(String namespace, String resourceName, HashMap resultMap) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        resultMap.put("sourceTypeYaml", resultString);

        return (Roles) commonService.setResultModel(commonService.setResultObject(resultMap, Roles.class), Constants.RESULT_STATUS_SUCCESS);
    }



    /**
     * Roles를 생성한다.
     *
     * @param namespace  the namespace
     * @param yaml  the yaml
     * @return  return is succeeded
     */
    public Object createRoles(String namespace, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class);

        return  commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_ROLES);
    }




    /**
     * Roles를 삭제한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param resultMap the result map
     * @return the resultStatus
     */
    public ResultStatus deleteRoles(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesDeleteUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                        ,HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_ROLES);
    }


    /**
     * Roles를 수정한다.
     *
     * @param namespace the namespace
     * @param resourceName the resource name
     * @param yaml  the yaml
     * @return the roles
     */
    public Object updateRoles(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesUpdateUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, Roles.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES_DETAIL.replace("{roleName:.+}", resourceName));
    }


}
