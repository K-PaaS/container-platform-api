package org.paasta.container.platform.api.users;

import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.clusters.clusters.Clusters;
import org.paasta.container.platform.api.clusters.clusters.ClustersService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.secret.Secrets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.paasta.container.platform.api.common.Constants.*;

/**
 * User Service 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.09.22
 **/
@Service
public class UsersService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersService.class);

    private final RestTemplateService restTemplateService;
    private final PropertyService propertyService;
    private final CommonService commonService;
    private final ResourceYamlService resourceYamlService;
    private final AccessTokenService accessTokenService;
    private final ClustersService clustersService;

    /**
     * Instantiates a new Users service
     *
     * @param restTemplateService the rest template service
     * @param propertyService     the property service
     * @param commonService       the common service
     * @param resourceYamlService the resource yaml service
     * @param accessTokenService  the access token service
     * @param clustersService     the clusters service
     */
    @Autowired
    public UsersService(RestTemplateService restTemplateService, PropertyService propertyService, CommonService commonService, ResourceYamlService resourceYamlService, AccessTokenService accessTokenService, ClustersService clustersService) {
        this.restTemplateService = restTemplateService;
        this.propertyService = propertyService;
        this.commonService = commonService;
        this.resourceYamlService = resourceYamlService;
        this.accessTokenService = accessTokenService;
        this.clustersService = clustersService;
    }


    /**
     * Users 전체 목록 조회(Get Users list)
     *
     * @param namespace the namespace
     * @return the users list
     */
    public UsersListAdmin getUsersAll(String namespace) {
        UsersListAdmin rsDb = restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST + "?namespace=" + namespace, HttpMethod.GET, null, UsersListAdmin.class);
        return (UsersListAdmin) commonService.setResultModel(commonService.setResultObject(rsDb, UsersListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * Users 전체 목록 조회(Get Users list for Admin)
     *
     * @param cluster    the cluster
     * @param userType   the user type
     * @param searchName the search name
     * @param limit      the limit
     * @param offset     the offset
     * @param orderBy    the orderBy
     * @param order      the order
     * @return the users list
     */
    public Object getUsersAllByCluster(String cluster, String userType, String searchName, int limit, int offset, String orderBy, String order) {

        if (SELECTED_ADMINISTRATOR.equalsIgnoreCase(userType)) {
            userType = AUTH_CLUSTER_ADMIN;
        } else if (SELECTED_USER.equalsIgnoreCase(userType)) {
            userType = AUTH_USER;
        } else {
            throw new IllegalArgumentException(MessageConstant.USER_TYPE_ILLEGALARGUMENT);
        }

        String reqUrlParam = "?userType=" + userType + "&searchParam=" + searchName + "&orderBy=" + orderBy + "&order=" + order;
        UsersListAdmin usersListAdmin = restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_CLUSTER.replace("{cluster:.+}", cluster) + reqUrlParam, HttpMethod.GET, null, UsersListAdmin.class);

        //users list paging
        usersListAdmin = commonService.userListProcessing(usersListAdmin, offset, limit, orderBy, order, searchName, UsersListAdmin.class);

        return commonService.setResultModel(commonService.setResultObject(usersListAdmin, UsersListAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * 각 Namespace 별 Users 목록 조회(Get Users namespace list)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the users list
     */
    public UsersListAdmin getUsersListByNamespaceAdmin(String cluster, String namespace) {
        return restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE
                .replace("{cluster:.+}", cluster)
                .replace("{namespace:.+}", namespace), HttpMethod.GET, null, UsersListAdmin.class);
    }


    /**
     * 각 Namespace 별 Users 목록 조회(Get Users namespace list)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the users list
     */
    public UsersList getUsersListByNamespace(String cluster, String namespace) {
        return restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE
                .replace("{cluster:.+}", cluster)
                .replace("{namespace:.+}", namespace), HttpMethod.GET, null, UsersList.class);
    }




    /**
     * 하나의 Cluster 내 여러 Namespaces 에 속한 User 에 대한 상세 조회(Get Users cluster namespace)
     *
     * @param cluster the cluster
     * @param userId the userId
     * @param limit the limit
     * @param offset the offset
     * @return the users detail
     */
    public Object getUsersInMultiNamespace(String cluster, String userId, int limit,  int offset) throws Exception {

        UsersAdmin usersAdmin = new UsersAdmin();
        Users usersByDefaultNamespace = null;

        String defaultNamespace = propertyService.getDefaultNamespace();

        try {
            //temp-namespace user info get
          usersByDefaultNamespace = restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS
                    .replace("{cluster:.+}", cluster)
                    .replace("{namespace:.+}", defaultNamespace)
                    .replace("{userId:.+}", userId), HttpMethod.GET, null, Users.class);
        }
        catch(Exception e) {
            return Constants.NOT_FOUND_RESULT_STATUS;
        }

        //set user info
        usersAdmin.setUserId(usersByDefaultNamespace.getUserId());
        usersAdmin.setServiceAccountName(usersByDefaultNamespace.getServiceAccountName());
        usersAdmin.setCreated(usersByDefaultNamespace.getCreated());

        //set cluster info
        usersAdmin.setClusterName(usersByDefaultNamespace.getClusterName());
        usersAdmin.setClusterApiUrl(usersByDefaultNamespace.getClusterApiUrl());
        usersAdmin.setClusterToken(usersByDefaultNamespace.getClusterToken());


        UsersList list = restTemplateService.send(TARGET_COMMON_API,
                Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);


        UsersAdmin.UsersDetails usersDetails;
        List<UsersAdmin.UsersDetails> usersDetailsList = new ArrayList<>();

        for (Users users : list.getItems()) {
            if (!propertyService.getIgnoreNamespaceList().contains(users.getCpNamespace())) {
                usersDetails = commonService.convert(users, UsersAdmin.UsersDetails.class);
                Object obj = restTemplateService.sendAdmin(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListSecretsGetUrl().replace("{namespace}", usersDetails.getCpNamespace()).replace("{name}", usersDetails.getSaSecret()), HttpMethod.GET, null, Map.class);

                if (!(obj instanceof ResultStatus)) {
                    // k8s에서 secret 정보 조회(Get secret from k8s)
                    Secrets secrets = (Secrets) commonService.setResultModel(commonService.setResultObject(obj, Secrets.class), Constants.RESULT_STATUS_SUCCESS);
                    usersDetails.setServiceAccountUid(secrets.getMetadata().getUid());
                    usersDetails.setSecrets(UsersAdmin.Secrets.builder()
                            .saSecret(secrets.getMetadata().getName())
                            .secretLabels(secrets.getMetadata().getLabels())
                            .secretType(secrets.getType()).build());

                }
                usersDetailsList.add(usersDetails);
            }
        }

        usersAdmin.setItems(usersDetailsList);
        usersAdmin = commonService.userListProcessing(usersAdmin, offset, limit, "", "", "", UsersAdmin.class);

        return commonService.setResultModel(commonService.setResultObject(usersAdmin, UsersAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * 각 Namespace 별 등록 되어 있는 사용자들의 이름 목록 조회(Get Users registered list namespace)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the users list
     */
    public Map<String, List> getUsersNameListByNamespace(String cluster, String namespace) {
        return restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_NAMES_LIST
                .replace("{cluster:.+}", cluster)
                .replace("{namespace:.+}", namespace), HttpMethod.GET, null, Map.class);
    }


    /**
     * Users 로그인을 위한 상세 조회(Get Users for login)
     *
     * @param userId  the userId
     * @param isAdmin the isAdmin
     * @return the users detail
     */
    public Users getUsersDetailsForLogin(String userId, String isAdmin) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DETAIL_LOGIN.replace("{userId:.+}", userId)
                        + "?isAdmin=" + isAdmin
                , HttpMethod.GET, null, Users.class);
    }


    /**
     * Namespace 관리자 상세 조회(Get Namespace Admin Users detail)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the users detail
     */
    public Users getUsersByNamespaceAndNsAdmin(String cluster, String namespace) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_BY_NAMESPACE_NS_ADMIN.replace("{cluster:.+}", cluster).replace("{namespace:.+}", namespace)
                , HttpMethod.GET, null, Users.class);
    }


    /**
     * Users 상세 조회(Get Users detail)
     *
     * @param userId the userId
     * @return the users detail
     */
    public UsersList getUsersDetails(String userId) {
        return restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", userId), HttpMethod.GET, null, UsersList.class);
    }


    /**
     * Namespace 와 userId로 사용자 단 건 상세 조회(Get Users userId namespace)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param userId    the userId
     * @return the users detail
     */
    public Users getUsers(String cluster, String namespace, String userId) {
        Users users = restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS
                .replace("{cluster:.+}", cluster)
                .replace("{namespace:.+}", namespace)
                .replace("{userId:.+}", userId), HttpMethod.GET, null, Users.class);
        return (Users) commonService.setResultModel(users, Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * 사용자 DB 저장(Save Users DB)
     *
     * @param users the users
     * @return return is succeeded
     */
    public ResultStatus createUsers(Users users) {
        return restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class);
    }


    /**
     * 사용자 권한 변경 DB 저장(Save Users DB)
     *
     * @param users the users
     * @return return is succeeded
     */
    public ResultStatus updateUsers(Users users) {
        return restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.PUT, users, ResultStatus.class);
    }

    /**
     * 사용자 생성(Create Users)
     * (Admin Portal)
     *
     * @param users the users
     * @return return is succeeded
     */
    public ResultStatus registerUsers(Users users) {
        List<Users.NamespaceRole> list = users.getSelectValues();
        ResultStatus rsDb = new ResultStatus();

        String defaultNamespace = propertyService.getDefaultNamespace();
        Users.NamespaceRole namespaceRole = new Users.NamespaceRole();
        namespaceRole.setNamespace(defaultNamespace);

        list.add(namespaceRole);

        for (Users.NamespaceRole nsRole : list) {
            String namespace = nsRole.getNamespace();
            String role = NOT_ASSIGNED_ROLE;

            String userName = users.getUserId();

            // 각 namespace 별 service account 생성(Create service account by each namespace name)
            resourceYamlService.createServiceAccount(userName, namespace);

            if(!StringUtils.isEmpty(nsRole.getRole())) {
                role = nsRole.getRole();
                // select box에서 선택한 role으로 role binding(Role binding selected role)
                resourceYamlService.createRoleBinding(userName, namespace, role);
            }

            String adminSaSecretName = restTemplateService.getSecretName(namespace, users.getUserId());

            users.setUserType(AUTH_USER);
            users.setCpNamespace(namespace);
            users.setServiceAccountName(userName);
            users.setRoleSetCode(role);
            users.setSaSecret(adminSaSecretName);
            users.setSaToken(accessTokenService.getSecrets(namespace, adminSaSecretName).getUserAccessToken());
            users.setIsActive(CHECK_Y);

            // DB에 저장(Save DB)
            rsDb = createUsers(commonSaveClusterInfo(propertyService.getCpClusterName(), users));

            // DB 커밋에 실패했을 경우 k8s 에 만들어진 service account 삭제(Delete service account)
            if (Constants.RESULT_STATUS_FAIL.equals(rsDb.getResultCode())) {
                LOGGER.info("DATABASE EXECUTE IS FAILED. K8S SERVICE ACCOUNT WILL BE REMOVED...");
                restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", userName), HttpMethod.DELETE, null, Object.class, true);
                return rsDb;
            }
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS);
    }


    /**
     * Users 수정(Update Users)
     *
     * @param cluster the cluster
     * @param userId  the userId
     * @param users   the users
     * @return return is succeeded
     * @throws Exception
     */
    public ResultStatus modifyUsersAdmin(String cluster, String userId, Users users) throws Exception {
        ResultStatus rsDb = new ResultStatus();
        String defaultNs = propertyService.getDefaultNamespace();

        Users tempUsers = getUsers(cluster, defaultNs, userId);
        tempUsers.setUserId(users.getUserId());
        tempUsers.setPassword(users.getPassword());
        tempUsers.setEmail(users.getEmail());

        createUsers(tempUsers);

        List<UsersAdmin.UsersDetails> usersDetails = ((UsersAdmin) getUsersInMultiNamespace(cluster, users.getServiceAccountName(), 0,0)).getItems();
        List<Users.NamespaceRole> selectValues = users.getSelectValues();

        // 기존 namespace list(Existed namespace list)
        List<String> defaultNsList = usersDetails.stream().map(UsersAdmin.UsersDetails::getCpNamespace).collect(Collectors.toList());

        // 넘어온 새로운 select value 중 namespace list(namespace list for new select value)
        List<String> newNsList = selectValues.stream().map(Users.NamespaceRole::getNamespace).collect(Collectors.toList());

        ArrayList<String> asIs = commonService.equalArrayList(defaultNsList, newNsList);
        ArrayList<String> toBeDelete = commonService.compareArrayList(defaultNsList, newNsList);
        ArrayList<String> toBeAdd = commonService.compareArrayList(newNsList, defaultNsList);

        List<Users.NamespaceRole> asIsNamespaces = new ArrayList<>();
        List<Users.NamespaceRole> toBeAddNamespace = new ArrayList<>();


        for (Users.NamespaceRole namespaceRole : selectValues) {
            Users.NamespaceRole namespaceRole2 = new Users.NamespaceRole();
            for (String name : asIs) {
                if (namespaceRole.getNamespace().equals(name)) {
                    namespaceRole2.setNamespace(namespaceRole.getNamespace());
                    namespaceRole2.setRole(namespaceRole.getRole());

                    asIsNamespaces.add(namespaceRole2);
                }
            }

            for (String name : toBeAdd) {
                if (namespaceRole.getNamespace().equals(name)) {
                    namespaceRole2.setNamespace(namespaceRole.getNamespace());
                    namespaceRole2.setRole(namespaceRole.getRole());

                    toBeAddNamespace.add(namespaceRole2);
                }
            }
        }

        for (Users.NamespaceRole nr : asIsNamespaces) {
            Users updateUser = getUsers(cluster, nr.getNamespace(), users.getServiceAccountName());
            String namespace = nr.getNamespace();
            String newRole = nr.getRole();
            String defaultRole = updateUser.getRoleSetCode();

            if (!updateUser.getRoleSetCode().equals(nr.getRole())) {
                LOGGER.info("Same Namespace >> {}, Default Role >> {}, New Role >> {}", namespace, defaultRole, newRole);
                restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", users.getServiceAccountName() + Constants.NULL_REPLACE_TEXT + defaultRole + "-binding"), HttpMethod.DELETE, null, Object.class, true);
                resourceYamlService.createRoleBinding(users.getServiceAccountName(), namespace, newRole);

                updateUser.setRoleSetCode(newRole);
                updateUser.setSaToken(accessTokenService.getSecrets(namespace, updateUser.getSaSecret()).getUserAccessToken());
            }

            updateUser.setUserId(users.getUserId());
            updateUser.setPassword(users.getPassword());
            updateUser.setEmail(users.getEmail());
            updateUser.setUserType(AUTH_USER);
            rsDb = createUsers(updateUser);
        }


        for (String deleteSa : toBeDelete) {
            LOGGER.info("Default Namespace's service account delete >> " + deleteSa);
            Users deleteUser = getUsers(cluster, deleteSa, users.getServiceAccountName());
            deleteUsers(deleteUser);
        }


        for (Users.NamespaceRole nr : toBeAddNamespace) {
            String addInNamespace = nr.getNamespace();
            String addRole = nr.getRole();
            String sa = users.getServiceAccountName();

            LOGGER.info("New Namespace create >> {}, New Role >> {}", addInNamespace, addRole);

            resourceYamlService.createServiceAccount(sa, addInNamespace);
            resourceYamlService.createRoleBinding(sa, addInNamespace, addRole);

            String saSecretName = restTemplateService.getSecretName(addInNamespace, sa);

            Users newUser = users;
            newUser.setId(0);
            newUser.setCpNamespace(addInNamespace);
            newUser.setRoleSetCode(addRole);
            newUser.setIsActive(CHECK_Y);
            newUser.setSaSecret(saSecretName);
            newUser.setSaToken(accessTokenService.getSecrets(addInNamespace, saSecretName).getUserAccessToken());
            newUser.setUserType(AUTH_USER);

            rsDb = createUsers(commonSaveClusterInfo(propertyService.getCpClusterName(), newUser));
        }

        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class), Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS_DETAIL.replace("{userId:.+}", users.getServiceAccountName()));
    }


    /**
     * Users 삭제(Delete Users)
     *
     * @param users the users
     * @return return is succeeded
     */
    public ResultStatus deleteUsers(Users users) {
        String namespace = users.getCpNamespace();
        String saName = users.getServiceAccountName();
        String role = users.getRoleSetCode();

        // 기존 service account 삭제(Delete Exited service account)
        restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", saName), HttpMethod.DELETE, null, Object.class, true);

        // role binding 삭제(Delete role binding)
        restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", saName + Constants.NULL_REPLACE_TEXT + role + "-binding"), HttpMethod.DELETE, null, Object.class, true);

        // DB delete
        ResultStatus rsDb = (ResultStatus) restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DELETE + users.getId(), HttpMethod.DELETE, null, Object.class);

        return rsDb;
    }


    /**
     * Users 수정(Update Users)
     *
     * @param userId the user id
     * @param user   the users
     * @return return is succeeded
     */
    public ResultStatus modifyUsers(String userId, Users user) {
        return restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", userId), HttpMethod.PUT, user, ResultStatus.class);
    }


    /**
     * Users 삭제(Delete Users)
     * (All Namespaces)
     *
     * @param userId the user id
     * @return return is succeeded
     */
    public ResultStatus deleteUsersByAllNamespaces(String userId) {
        UsersList users = getUsersDetails(userId);

        ResultStatus rs = new ResultStatus();
        for (Users user : users.getItems()) {
            rs = deleteUsers(user);
        }
        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rs, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS);
    }


    /**
     * Users 권한 설정(Set Users authority)
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @param users     the users
     * @return return is succeeded
     */
    public ResultStatus modifyUsersConfig(String cluster, String namespace, List<Users> users) {
        ResultStatus rsDb = null;

        List<Users> defaultUserList = getUsersListByNamespace(cluster, namespace).getItems();

        List<String> defaultUserNameList = defaultUserList.stream().map(Users::getServiceAccountName).collect(Collectors.toList());
        List<String> newUserNameList = users.stream().map(Users::getServiceAccountName).collect(Collectors.toList());

        ArrayList<String> toBeDelete = (ArrayList<String>) defaultUserNameList.stream().filter(x -> !newUserNameList.contains(x)).collect(Collectors.toList());
        ArrayList<String> toBeAdd = (ArrayList<String>) newUserNameList.stream().filter(x -> !defaultUserNameList.contains(x)).collect(Collectors.toList());

        for (Users value : defaultUserList) {
            for (Users u : users) {
                String sa = u.getServiceAccountName();
                String role = u.getRoleSetCode();

                if (value.getServiceAccountName().equals(sa)) {
                    if (!value.getRoleSetCode().equals(role)) {
                        LOGGER.info("Update >>> sa :: {}, role :: {}", sa, role);

                        Users updatedUser = getUsers(cluster, namespace, sa);

                        // remove default roleBinding, add new roleBinding
                        restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", sa + Constants.NULL_REPLACE_TEXT + value.getRoleSetCode() + "-binding"), HttpMethod.DELETE, null, Object.class, true);

                        updateSetRoleUser(namespace, sa, role, updatedUser);
                        updatedUser.setRoleSetCode(role);
                        rsDb = updateUsers(updatedUser);
                    }
                }
            }

            for (String s : toBeDelete) {
                if (s.equals(value.getServiceAccountName())) {
                    String saName = value.getServiceAccountName();
                    String roleName = value.getRoleSetCode();

                    LOGGER.info("Delete >>> sa :: {}, role :: {}", saName, roleName);

                    restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", namespace).replace("{name}", saName), HttpMethod.DELETE, null, Object.class, true);
                    restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", namespace).replace("{name}", saName + Constants.NULL_REPLACE_TEXT + roleName + "-binding"), HttpMethod.DELETE, null, Object.class, true);

                    rsDb = restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS.replace("{cluster:.+}", cluster).replace("{namespace:.+}", namespace).replace("{userId:.+}", saName), HttpMethod.DELETE, null, ResultStatus.class);
                }
            }
        }

        for (Users user : users) {
            for (String s : toBeAdd) {
                if (s.equals(user.getServiceAccountName())) {
                    String saName = user.getServiceAccountName();
                    String roleName = user.getRoleSetCode();

                    LOGGER.info("Add >>> sa :: {}, role :: {}", saName, roleName);

                    UsersList usersList = getUsersDetails(saName);
                    Users newUser = usersList.getItems().get(0);

                    resourceYamlService.createServiceAccount(saName, namespace);

                    updateSetRoleUser(namespace, saName, roleName, newUser);
                    newUser.setId(0);
                    newUser.setCpNamespace(namespace);
                    newUser.setRoleSetCode(roleName);
                    newUser.setIsActive(CHECK_Y);
                    newUser.setUserType(AUTH_USER);

                    rsDb = updateUsers(newUser);
                }
            }
        }


        return (ResultStatus) commonService.setResultModelWithNextUrl(commonService.setResultObject(rsDb, ResultStatus.class),
                Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS_CONFIG);
    }


    /**
     * Role 에 따른 사용자 권한 설정(Setting Role to User)
     *
     * @param namespace the namespace
     * @param saName    the service account name
     * @param roleName  the role name
     * @param newUser   the new User object
     */
    private void updateSetRoleUser(String namespace, String saName, String roleName, Users newUser) {
        if (!Constants.NOT_ASSIGNED_ROLE.equals(roleName)) {
            resourceYamlService.createRoleBinding(saName, namespace, roleName);
            String saSecretName = restTemplateService.getSecretName(namespace, saName);
            newUser.setSaSecret(saSecretName);
            newUser.setSaToken(accessTokenService.getSecrets(namespace, saSecretName).getUserAccessToken());
        } else {
            newUser.setSaSecret(Constants.NOT_ASSIGNED_ROLE);
            newUser.setSaToken(Constants.NOT_ASSIGNED_ROLE);
        }

    }


    /**
     * Namespace 상세 Users 목록 조회(Get Users namespace list)
     *
     * @param cluster    the cluster
     * @param namespace  the namespace
     * @param offset     the offset
     * @param limit      the limit
     * @param orderBy    the orderBy
     * @param order      the order
     * @param searchName the search name
     * @return the users list
     */
    public Object getUsersListInNamespaceAdmin(String cluster, String namespace, int offset, int limit, String orderBy, String order, String searchName) {

         String param ="?orderBy=" + orderBy + "&order=" + order + "&searchName=" + searchName;

        UsersListInNamespaceAdmin usersListInNamespaceAdmin = restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE
                .replace("{cluster:.+}", cluster)
                .replace("{namespace:.+}", namespace) + param, HttpMethod.GET, null, UsersListInNamespaceAdmin.class);

        //users list paging
        usersListInNamespaceAdmin = commonService.userListProcessing(usersListInNamespaceAdmin, offset, limit, orderBy, order, searchName, UsersListInNamespaceAdmin.class);
        return commonService.setResultModel(commonService.setResultObject(usersListInNamespaceAdmin, UsersListInNamespaceAdmin.class), Constants.RESULT_STATUS_SUCCESS);
    }


    /**
     * 해당 클러스터 정보의 값을 사용자에 저장
     *
     * @param clusterName the cluster name
     * @param users       the users
     * @return the users
     */
    public Users commonSaveClusterInfo(String clusterName, Users users) {
        Clusters clusters = clustersService.getClusters(clusterName);

        users.setClusterApiUrl(clusters.getClusterApiUrl());
        users.setClusterName(clusters.getClusterName());
        users.setClusterToken(clusters.getClusterToken());

        return users;
    }


    /**
     * 특정 Namespace 관리자 판별이 포함된 Users Name 목록 조회
     *
     * @param cluster   the cluster
     * @param namespace the namespace
     * @return the UsersInNamespace
     */
    public UsersInNamespace getUsersNameListByNamespaceAdmin(String cluster, String namespace) {
        UsersInNamespace usersInNamespace = new UsersInNamespace();
        usersInNamespace.setNamespace(namespace);

        List<UsersInfo> usersInfos = new ArrayList<>();

        Map<String, List<String>> list = restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_NAMES, HttpMethod.GET, null, Map.class);
        List<String> names = list.get(USERS);

        if (ALL_NAMESPACES.equals(namespace)) {
            for (String name : names) {
                UsersInfo usersInfo = new UsersInfo();
                usersInfo.setUserId(name);
                usersInfo.setIsNsAdmin(CHECK_N);

                usersInfos.add(usersInfo);
            }
        } else {
            Users user = getUsersByNamespaceAndNsAdmin(cluster, namespace);

            for (String name : names) {
                UsersInfo usersInfo = new UsersInfo();
                usersInfo.setUserId(name);
                usersInfo.setIsNsAdmin(CHECK_N);
                if (name.contains(user.getUserId())) {
                    usersInfo.setIsNsAdmin(CHECK_Y);
                }

                usersInfos.add(usersInfo);
            }
        }

        usersInNamespace.setUsersInfo(usersInfos);

        return (UsersInNamespace) commonService.setResultModel(usersInNamespace, Constants.RESULT_STATUS_SUCCESS);
    }

}
