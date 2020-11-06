package org.paasta.container.platform.api.roles;

import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.paasta.container.platform.api.common.Constants.URI_COMMON_API_NAMESPACES_ROLE_BY_CLUSTER_NAME_USER_ID;

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
     * Instantiates a new Roles service
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
     * Roles 목록 조회(Get Roles list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the roles list
     */
    public RolesList getRolesList(String namespace, int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);

        RolesList roleList = commonService.setResultObject(responseMap, RolesList.class);
        roleList = commonService.resourceListProcessing(roleList, offset, limit, orderBy, order, searchName, RolesList.class);


        return (RolesList) commonService.setResultModel(roleList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Roles 상세 조회(Get Roles detail)
     *
     * @param namespace    the namespace
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
     * Roles YAML 조회(Get Roles yaml)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param resultMap    the result map
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
     * Roles 생성(Create Roles)
     *
     * @param namespace the namespace
     * @param yaml      the yaml
     * @return return is succeeded
     */
    public Object createRoles(String namespace, String yaml, boolean isAdmin) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesCreateUrl()
                        .replace("{namespace}", namespace), HttpMethod.POST, yaml, Object.class, isAdmin);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_ROLES);
    }


    /**
     * Roles 삭제(Delete Roles)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param resultMap    the result map
     * @return return is succeeded
     */
    public ResultStatus deleteRoles(String namespace, String resourceName, HashMap resultMap) {
        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesDeleteUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.DELETE, null, ResultStatus.class, true);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_ROLES);
    }


    /**
     * Roles 수정(Update Roles)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @param yaml         the yaml
     * @return return is succeeded
     */
    public Object updateRoles(String namespace, String resourceName, String yaml) {
        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesUpdateUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName), HttpMethod.PUT, yaml, Object.class, true);

        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, Roles.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_SERVICES_DETAIL.replace("{roleName:.+}", resourceName));
    }


    //methods for administrators

    /**
     * Roles Admin 목록 조회(Get Roles Admin list)
     *
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the roles admin list
     */
    public Object getRolesListAdmin(String namespace, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesListUrl()
                        .replace("{namespace}", namespace)
                , HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }


        RolesListAdmin rolesListAdmin = commonService.setResultObject(responseMap, RolesListAdmin.class);
        rolesListAdmin = commonService.resourceListProcessing(rolesListAdmin, offset, limit, orderBy, order, searchName, RolesListAdmin.class);
        return commonService.setResultModel(rolesListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Roles Admin 상세 조회(Get Roles Admin detail)
     *
     * @param namespace    the namespace
     * @param resourceName the resource name
     * @return the roles admin
     */
    public Object getRolesAdmin(String namespace, String resourceName) {

        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesGetUrl()
                        .replace("{namespace}", namespace)
                        .replace("{name}", resourceName)
                , HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, RolesAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * 전체 Namespaces 의 Roles Admin 목록 조회(Get Roles Admin list in all namespaces)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the roles admin list
     */
    public Object getRolesListAllNamespacesAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        RolesListAdmin rolesListAdmin = commonService.setResultObject(responseMap, RolesListAdmin.class);
        rolesListAdmin = commonService.resourceListProcessing(rolesListAdmin, offset, limit, orderBy, order, searchName, RolesListAdmin.class);
        return commonService.setResultModel(rolesListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * User가 속해 있는 Namespace와 Role 목록 조회(Get Namespace and Roles List to which User belongs)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param userId     the user id
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return return is succeeded
     */
    public Object getRolesListAllNamespacesAdminByUserId(String cluster, String namespace, String userId, int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListRolesListAllNamespacesUrl(), HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        RolesListAllNamespaces rolesListAllNamespaces = commonService.setResultObject(responseMap, RolesListAllNamespaces.class);

        UsersList usersList = restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, URI_COMMON_API_NAMESPACES_ROLE_BY_CLUSTER_NAME_USER_ID
                .replace("{cluster:.+}", cluster)
                .replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);

        for (Users user:usersList.getItems()) {
            for (RolesListAllNamespaces.RolesListAllNamespacesItem item:rolesListAllNamespaces.getItems()) {
                item.setCheckYn(Constants.CHECK_N);
                item.setUserType(Constants.NOT_ASSIGNED_ROLE);
                if(user.getCpNamespace().equals(item.getNamespace()) && user.getRoleSetCode().equals(item.getName())) {
                    item.setCheckYn(Constants.CHECK_Y);
                    item.setUserType(user.getUserType());
                }
            }
        }

        rolesListAllNamespaces = commonService.resourceListProcessing(rolesListAllNamespaces, offset, limit, orderBy, order, searchName, RolesListAllNamespaces.class);
        return commonService.setResultModel(rolesListAllNamespaces, Constants.RESULT_STATUS_SUCCESS);
    }
}
