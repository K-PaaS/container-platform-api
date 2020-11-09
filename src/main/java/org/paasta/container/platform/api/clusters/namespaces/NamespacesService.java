package org.paasta.container.platform.api.clusters.namespaces;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.clusters.limitRanges.LimitRangesList;
import org.paasta.container.platform.api.clusters.limitRanges.LimitRangesService;
import org.paasta.container.platform.api.clusters.namespaces.support.NamespacesListSupport;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasList;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.paasta.container.platform.api.common.Constants.*;

/**
 * Namespaces Service 클래스
 *
 * @author jjy
 * @version 1.0
 * @since 2020.10.14
 */
@Service
public class NamespacesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(NamespacesService.class);

    private final RestTemplateService restTemplateService;
    private final CommonService commonService;
    private final PropertyService propertyService;
    private final ResourceYamlService resourceYamlService;
    private final UsersService usersService;
    private final AccessTokenService accessTokenService;
    private final ResourceQuotasService resourceQuotasService;
    private final LimitRangesService limitRangesService;

    /**
     * Instantiates a new Namespace service
     *
     * @param restTemplateService   the rest template service
     * @param commonService         the common service
     * @param propertyService       the property service
     * @param resourceYamlService   the resource yaml service
     * @param usersService          the users service
     * @param accessTokenService    the access token service
     * @param resourceQuotasService the resource quotas service
     * @param limitRangesService    the limit ranges service
     */
    @Autowired
    public NamespacesService(RestTemplateService restTemplateService, CommonService commonService, PropertyService propertyService, ResourceYamlService resourceYamlService, UsersService usersService, AccessTokenService accessTokenService, ResourceQuotasService resourceQuotasService, LimitRangesService limitRangesService) {
        this.restTemplateService = restTemplateService;
        this.commonService = commonService;
        this.propertyService = propertyService;
        this.resourceYamlService = resourceYamlService;
        this.usersService = usersService;
        this.accessTokenService = accessTokenService;
        this.resourceQuotasService = resourceQuotasService;
        this.limitRangesService = limitRangesService;
    }

    /**
     * Namespaces 상세 조회(Get Namespaces detail)
     *
     * @param namespace the namespaces
     * @return the namespaces detail
     */
    Namespaces getNamespaces(String namespace) {
        HashMap resultMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        return (Namespaces) commonService.setResultModel(commonService.setResultObject(resultMap, Namespaces.class), Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * NameSpaces 상세 조회(Get NameSpaces Admin detail)
     *
     * @param namespace the namespaces
     * @return the namespaces admin
     */
    public Object getNamespacesAdmin(String namespace) {
        Object obj = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, Map.class);

        HashMap responseMap;

        try {
            responseMap = (HashMap) obj;
        } catch (Exception e) {
            return obj;
        }

        return commonService.setResultModel(commonService.setResultObject(responseMap, NamespacesAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * NameSpaces 목록 조회(Get NameSpaces list)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the namespaces list
     */
    public NamespacesList getNamespacesList(int offset, int limit, String orderBy, String order, String searchName) {

        HashMap responseMap = (HashMap) restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesListUrl()
                , HttpMethod.GET, null, Map.class);

        NamespacesList namespacesList = commonService.setResultObject(responseMap, NamespacesList.class);
        namespacesList = commonService.resourceListProcessing(namespacesList, offset, limit, orderBy, order, searchName, NamespacesList.class);

        return (NamespacesList) commonService.setResultModel(namespacesList, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * NameSpaces Admin 목록 조회(Get NameSpaces Admin list)
     *
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the searchName
     * @return the namespaces admin list
     */
    public Object getNamespacesListAdmin(int offset, int limit, String orderBy, String order, String searchName) {
        HashMap responseMap = null;

        Object response = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesListUrl(),
                HttpMethod.GET, null, Map.class);

        try {
            responseMap = (HashMap) response;
        } catch (Exception e) {
            return response;
        }

        NamespacesListAdmin namespacesListAdmin = commonService.setResultObject(responseMap, NamespacesListAdmin.class);
        namespacesListAdmin = commonService.resourceListProcessing(namespacesListAdmin, offset, limit, orderBy, order, searchName, NamespacesListAdmin.class);

        return commonService.setResultModel(namespacesListAdmin, Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * NameSpaces YAML 조회(Get NameSpaces yaml)
     *
     * @param namespace the namespace
     * @return the namespaces yaml
     */
    public NamespacesYaml getNamespacesYaml(String namespace) {
        String resultString = restTemplateService.send(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesGetUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("sourceTypeYaml", resultString);

        return (NamespacesYaml) commonService.setResultModel(commonService.setResultObject(resultMap, NamespacesYaml.class), Constants.RESULT_STATUS_SUCCESS);
    }

//    /**
//     * NameSpace 생성(Create NameSpaces)
//     *
//     * @param yaml the yaml
//     * @return return is succeeded
//     */
//    public Object createNamespaces(String yaml) {
//        Object map = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
//                propertyService.getCpMasterApiListNamespacesCreateUrl(), HttpMethod.POST, yaml, Object.class, true);
//
//        return commonService.setResultModelWithNextUrl(commonService.setResultObject(map, ResultStatus.class),
//                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
//    }

    /**
     * NameSpaces 삭제(Delete NameSpaces)
     *
     * @param namespace the namespace
     * @return return is succeeded
     */
    public ResultStatus deleteNamespaces(String namespace) {
        ResultStatus resultStatus = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListNamespacesDeleteUrl()
                        .replace("{name}", namespace), HttpMethod.DELETE, null, ResultStatus.class);

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
    }

//    /**
//     * NameSpaces 수정(Update NameSpaces)
//     *
//     * @param namespace the namespace
//     * @param yaml      the yaml
//     * @return return is succeeded
//     */
//    public ResultStatus updateNamespaces(String namespace, String yaml) {
//        ResultStatus resultStatus = restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API,
//                propertyService.getCpMasterApiListNamespacesUpdateUrl()
//                        .replace("{name}", namespace), HttpMethod.PUT, yaml, ResultStatus.class, true);
//        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class),
//                Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES);
//
//    }


    /**
     * Namespaces 생성(Create Namespaces)
     *
     * @param initTemplate the initTemplate
     * @return return is succeeded
     */
    public ResultStatus createInitNamespaces(NamespacesInitTemplate initTemplate) {
        String namespace = initTemplate.getName();
        String nsAdminUserId = initTemplate.getNsAdminUserId();

        resourceYamlService.createNamespace(namespace);

        resourceYamlService.createInitRole(namespace);
        resourceYamlService.createNsAdminRole(namespace);

        ResultStatus saResult = resourceYamlService.createServiceAccount(nsAdminUserId, namespace);

        if (Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
            return saResult;
        }

        ResultStatus rbResult = resourceYamlService.createRoleBinding(nsAdminUserId, namespace, Constants.DEFAULT_NAMESPACE_ADMIN_ROLE);

        if (Constants.RESULT_STATUS_FAIL.equals(rbResult.getResultCode())) {
            LOGGER.info("CLUSTER ROLE BINDING EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", nsAdminUserId), HttpMethod.DELETE, null, Object.class, true);
            return rbResult;
        }


        for (String rq : initTemplate.getResourceQuotasList()) {
            if (DEFAULT_RESOURCE_QUOTAS_LIST.contains(rq)) {
                resourceYamlService.createDefaultResourceQuota(namespace, rq);
            }
        }

        for (String lr : initTemplate.getLimitRangesList()) {
            if (DEFAULT_LIMIT_RANGES_LIST.contains(lr)) {
                resourceYamlService.createDefaultLimitRanges(namespace, lr);
            }
        }

        String saSecretName = restTemplateService.getSecretName(namespace, nsAdminUserId);

        Users newNsUser = usersService.getUsers(DEFAULT_NAMESPACE_NAME, nsAdminUserId);
        newNsUser.setId(0);
        newNsUser.setCpNamespace(namespace);
        newNsUser.setRoleSetCode(DEFAULT_NAMESPACE_ADMIN_ROLE);
        newNsUser.setSaSecret(saSecretName);
        newNsUser.setSaToken(accessTokenService.getSecrets(namespace, saSecretName).getUserAccessToken());
        newNsUser.setUserType(AUTH_NAMESPACE_ADMIN);
        newNsUser.setIsActive(CHECK_Y);

        ResultStatus rsDb = usersService.createUsers(usersService.commonSaveClusterInfo(Constants.SINGLE_CLUSTER_NAME, newNsUser));

        if (Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
            LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT, CLUSTER ROLE BINDING WILL BE REMOVED...");
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListNamespacesDeleteUrl().replace("{namespace}", namespace), HttpMethod.DELETE, null, Object.class, true);
            restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListClusterRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", nsAdminUserId + "-" + DEFAULT_NAMESPACE_ADMIN_ROLE + "-binding"), HttpMethod.DELETE, null, Object.class, true);
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, "YOUR_NAMESPACES_LIST_PAGE");
    }


    /**
     * Namespaces 수정(modify Namespaces)
     *
     * @param namespace    the namespace
     * @param initTemplate the init template
     * @return return is succeeded
     */
    public ResultStatus modifyInitNamespaces(String cluster, String namespace, NamespacesInitTemplate initTemplate) {
        ResultStatus resultStatus = new ResultStatus();

        modifyResourceQuotas(namespace, initTemplate.getResourceQuotasList());
        modifyLimitRanges(namespace, initTemplate.getLimitRangesList());

        String nsAdminUserId = initTemplate.getNsAdminUserId();
        Users nsAdminUser = usersService.getUsersByNamespaceAndNsAdmin(cluster, namespace);

        if (!nsAdminUser.getUserId().equals(initTemplate.getNsAdminUserId())) {
            usersService.deleteUsers(nsAdminUser);
            // add new sa
            ResultStatus saResult = resourceYamlService.createServiceAccount(nsAdminUserId, namespace);

            if (Constants.RESULT_STATUS_FAIL.equals(saResult.getResultCode())) {
                return saResult;
            }

            ResultStatus rbResult = resourceYamlService.createRoleBinding(nsAdminUserId, namespace, Constants.DEFAULT_NAMESPACE_ADMIN_ROLE);

            if (Constants.RESULT_STATUS_FAIL.equals(rbResult.getResultCode())) {
                LOGGER.info("CLUSTER ROLE BINDING EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
                restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", nsAdminUserId), HttpMethod.DELETE, null, Object.class, true);
                return rbResult;
            }
            String saSecretName = restTemplateService.getSecretName(namespace, nsAdminUserId);

            Users newNsUser = usersService.getUsers(DEFAULT_NAMESPACE_NAME, nsAdminUserId);
            newNsUser.setId(0);
            newNsUser.setCpNamespace(namespace);
            newNsUser.setRoleSetCode(DEFAULT_NAMESPACE_ADMIN_ROLE);
            newNsUser.setSaSecret(saSecretName);
            newNsUser.setSaToken(accessTokenService.getSecrets(namespace, saSecretName).getUserAccessToken());
            newNsUser.setUserType(AUTH_NAMESPACE_ADMIN);
            newNsUser.setIsActive(CHECK_Y);

            resultStatus = usersService.createUsers(usersService.commonSaveClusterInfo(Constants.SINGLE_CLUSTER_NAME, newNsUser));

        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(resultStatus, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, "YOUR_NAMESPACES_DETAIL_PAGE");
    }


    /**
     * ResourceQuotas 변경
     *
     * @param namespace            the namespace
     * @param requestUpdatedRqList the request update resourceQuotas list
     */
    private void modifyResourceQuotas(String namespace, List<String> requestUpdatedRqList) {
        ResourceQuotasList resourceQuotasList = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListResourceQuotasListUrl()
                        .replace("{namespace}", namespace), HttpMethod.GET, null, ResourceQuotasList.class);

        List<String> k8sResourceQuotasList = resourceQuotasList.getItems().stream().map(a -> a.getMetadata().getName()).collect(Collectors.toList());

        ArrayList<String> toBeDelete = compareArrayList(k8sResourceQuotasList, requestUpdatedRqList);
        ArrayList<String> toBeAdd = compareArrayList(requestUpdatedRqList, k8sResourceQuotasList);

        // delete
        for (String deleteRqName : toBeDelete) {
            resourceQuotasService.deleteResourceQuotas(namespace, deleteRqName);
        }

        // add
        for (String rqName : toBeAdd) {
            resourceYamlService.createDefaultResourceQuota(namespace, rqName);
        }
    }


    /**
     * LimitRanges 변경
     *
     * @param namespace            the namespace
     * @param requestUpdatedLrList the request update limitRanges list
     */
    private void modifyLimitRanges(String namespace, List<String> requestUpdatedLrList) {
        LimitRangesList limitRangesList = restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API,
                propertyService.getCpMasterApiListLimitRangesListUrl().replace("{namespace}", namespace),
                HttpMethod.GET, null, LimitRangesList.class);

        List<String> k8sLimitRangesList = limitRangesList.getItems().stream().map(lr -> lr.getMetadata().getName()).collect(Collectors.toList());

        ArrayList<String> toBeDelete = compareArrayList(k8sLimitRangesList, requestUpdatedLrList);
        ArrayList<String> toBeAdd = compareArrayList(requestUpdatedLrList, k8sLimitRangesList);

        for (String lrName : toBeAdd) {
            resourceYamlService.createDefaultLimitRanges(namespace, lrName);
        }

        for (String deleteLrName : toBeDelete) {
            limitRangesService.deleteLimitRanges(namespace, deleteLrName);
        }
    }


    /**
     * 변경할 Resource 목록 값 비교
     *
     * @param defaultList the default list
     * @param compareList the compare list
     * @return the ArrayList
     */
    private ArrayList<String> compareArrayList(List<String> defaultList, List<String> compareList) {
        return (ArrayList<String>) defaultList.stream().filter(x -> !compareList.contains(x)).collect(Collectors.toList());
    }


    /**
     * Namespace Selectbox를 위한 전체 목록 조회
     *
     * @return the namespaces admin
     */
    public Object getNamespacesListForSelectbox() {
        NamespacesListAdmin namespacesListAdmin = (NamespacesListAdmin) getNamespacesListAdmin(0, 0, "name", "asc", "");
        List<NamespacesListAdminItem> namespaceItem = namespacesListAdmin.getItems();

        List<String> returnNamespaceList = new ArrayList<>();
        NamespacesListSupport namespacesListSupport = new NamespacesListSupport();

        for (NamespacesListAdminItem n : namespaceItem) {
            returnNamespaceList.add(n.getName());
        }

        namespacesListSupport.setItems(returnNamespaceList);


        return commonService.setResultModel(namespacesListSupport, Constants.RESULT_STATUS_SUCCESS);
    }

}
