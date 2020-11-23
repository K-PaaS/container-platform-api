package org.paasta.container.platform.api.users;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.paasta.container.platform.api.common.Constants.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import org.paasta.container.platform.api.accessInfo.AccessToken;
import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.clusters.clusters.Clusters;
import org.paasta.container.platform.api.clusters.clusters.ClustersService;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.MessageConstant;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.ResourceYamlService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;

/**
 *  Users Service Test 클래스
 *
 * @author hrjin
 * @version 1.0
 * @since 2020.11.13
 **/
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class UsersServiceTest {
    private static final String CLUSTER = "cp-cluster";
    private static final String CLUSTER_API_URL = "111.111.111.111:6443";
    private static final String CLUSTER_ADMIN_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IktNWmgxVXB3ajgwS0NxZjFWaVZJVGVvTXJoWnZ5dG0tMGExdzNGZjBKX00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJwYWFzLWYxMGU3ZTg4LTQ4YTUtNGUyYy04Yjk5LTZhYmIzY2ZjN2Y2Zi1jYWFzIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InN1cGVyLWFkbWluLXRva2VuLWtzbXo1Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InN1cGVyLWFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiMjMwZWQ1OGQtNzc0MC00MDI4LTk0MTEtYTM1MzVhMWM0NjU4Iiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OnBhYXMtZjEwZTdlODgtNDhhNS00ZTJjLThiOTktNmFiYjNjZmM3ZjZmLWNhYXM6c3VwZXItYWRtaW4ifQ.nxnIJCOH_XVMK71s0gF8bgzSxA7g6_y7hGdboLvSqIAGf9J9AgG1DouP29uShK19fMsl9IdbGODPvtuiBz4QyGLPARZldmlzEyFG3k08UMNay1xX_oK-Fe7atMlYgvoGzyM_5-Zp5dyvnxE2skk524htMGHqW1ZwnHLVxtBg8AuGfMwLW1xahmktsNZDG7pRMasPsj73E85lfavMobBlcs4hwVcZU82gAg0SK1QVe7-Uc2ip_9doNo6_9rGW3FwHdVgUNAeCvPRGV0W1dKJv0IX5e_7fIPIznj2xXcZoHf3BnKfDayDIKJOCdsEsy_2NGi1tiD3UvzDDzZpz02T2sg";
    private static final String NAMESPACE = "cp-namespace";
    private static final String ALL_NAMESPACES = "all";
    private static final String USER_ID = "paasta";
    private static final String SERVICE_ACCOUNT_NAME = "paasta-sa";
    private static final String CREATED = "10-10-10";
    private static final String ROLE = "paas-ta-container-platform-init-role";
    private static final String ADMIN_ROLE = "paas-ta-container-platform-admin-role";
    private static final String SECRET_NAME = "paasta-token-jqrx4";
    private static final String SECRET_TOKEN = "eyJhbGciOiJSUzI1NiIsImtpZCI6IktNWmgxVXB3ajgwS0NxZjFWaVZJVGVvTXJoWnZ5dG0tMGExdzNGZjBKX00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ0ZW1wLW5hbWVzcGFjZSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJ0ZXN0LXRva2VuLWpxcng0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InRlc3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI3Y2Q0Nzk4OC01YWViLTQ1ODQtYmNmOS04OTkwZTUzNWEzZGIiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6dGVtcC1uYW1lc3BhY2U6dGVzdCJ9.ZEwhnscTtPW6WrQ5I7fFWcsLWEqnilw7I8i7C4aSXElFHd583OQqTYGk8RUJU7UM6b2T8oKstejkLWE9xP3TchYyG5T-omZBCMe00JZIof4tp0MRZLgBhXizYXGvLb2bcMdlcWg2bCCVRO92Hjik-r-vqfaGbsRGx4dT2dk1sI4RA-XDnMsVFJS94V9P58cBupT1gRMrwWStrqlXrbiwgfIlGbU9GXnA07JUCMy-1wUYdMmRaICdj-Q7eNZ5BmKCNsFBcJKaDl5diNw-gSka2F61sywpezU-30sWAtRHYIYZt6PaAaZ4caAdR8f43Yq1m142RWsr3tunLgQ768UNtQ";

    private static final int OFFSET = 0;
    private static final int LIMIT = 1;
    private static final String ORDER_BY = "creationTime";
    private static final String ORDER = "desc";
    private static final String SEARCH_NAME = "";
    private static final boolean isAdmin = true;
    private static final String isAdminString = "true";
    private static final String isNotAdmin = "false";

    private static final String USER_TYPE_AUTH_CLUSTER_ADMIN = "administrator";
    private static final String USER_TYPE_AUTH_USER = "user";
    private static final String USER_TYPE_AUTH_NONE = "manager";
    private static final List<String> IGNORE_NAMESPACE_LIST = Collections.unmodifiableList(new ArrayList<String>(){
        {
            add("kubernetes-dashboard");
            add("kube-node-lease");
            add("kube-public");
            add("kube-system");
            add("default");
            add("temp-namespace");
        }
    });

    private static Users users = new Users();
    private static Users modifyUsers = new Users();

    private static UsersList usersList = new UsersList();
    private static UsersList modifyUsersList = new UsersList();

    private static UsersListAdmin usersListAdmin = new UsersListAdmin();
    private static UsersListAdmin finalUsersListAdmin = new UsersListAdmin();

    private static HashMap gResultMap = null;
    private static HashMap gResultNamesMap = null;

    private static ResultStatus gResultStatusModel = null;
    private static ResultStatus gResultFailStatusModel = null;

    private static List<String> gIgnoreNamespaceList = null;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @Mock
    ResourceYamlService resourceYamlService;

    @Mock
    AccessTokenService accessTokenService;

    @Mock
    ClustersService clustersService;

    @InjectMocks
    @Spy
    UsersService usersService;

    @Before
    public void setUp() {
        gResultMap = new HashMap();
        gResultNamesMap = new HashMap();
        gResultNamesMap.put("users", Arrays.asList("test", "paasta"));

        users = UsersModel.getResultUserWithClusterInfo();
        users.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        modifyUsers = UsersModel.getResultModifyUsersList();

        usersList = UsersModel.getResultUsersList();
        users.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        modifyUsersList = UsersModel.getResultUsersList();
        users.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        usersListAdmin = UsersModel.getResultUsersAdminList();
        finalUsersListAdmin = usersListAdmin;
        finalUsersListAdmin.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        finalUsersListAdmin.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        finalUsersListAdmin.setHttpStatusCode(CommonStatusCode.OK.getCode());
        finalUsersListAdmin.setDetailMessage(CommonStatusCode.OK.getMsg());

        gResultStatusModel = new ResultStatus();
        gResultStatusModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gResultStatusModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gResultStatusModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gResultStatusModel.setDetailMessage(CommonStatusCode.OK.getMsg());

        gResultFailStatusModel = new ResultStatus();
        gResultFailStatusModel.setResultCode(Constants.RESULT_STATUS_FAIL);
        gResultFailStatusModel.setResultMessage(Constants.RESULT_STATUS_FAIL);

        gIgnoreNamespaceList = new ArrayList<String>();
        gIgnoreNamespaceList.add("default");
        gIgnoreNamespaceList.add("kubernetes-dashboard");
        gIgnoreNamespaceList.add("kube-node-lease");
        gIgnoreNamespaceList.add("kube-public");
        gIgnoreNamespaceList.add("kube-system");
        gIgnoreNamespaceList.add("paas-ta-container-platform-temp-namespace");
        gIgnoreNamespaceList.add("cp-namespace");
    }

    @Test
    public void getUsersAll() {
        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, "/users?namespace=" + NAMESPACE, HttpMethod.GET, null, UsersListAdmin.class)).thenReturn(usersListAdmin);
        when(commonService.setResultObject(usersListAdmin, UsersListAdmin.class)).thenReturn(usersListAdmin);
        when(commonService.setResultModel(usersListAdmin, Constants.RESULT_STATUS_SUCCESS)).thenReturn(finalUsersListAdmin);

        UsersListAdmin resultList = usersService.getUsersAll(NAMESPACE);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    @Test
    public void getUsersAllByCluster_Cluster_Admin() {
        String reqUrlParam = "?userType=" + AUTH_CLUSTER_ADMIN + "&searchParam=" + SEARCH_NAME + "&orderBy=" + ORDER_BY + "&order=" + ORDER;

        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_CLUSTER.replace("{cluster:.+}", CLUSTER) + reqUrlParam , HttpMethod.GET, null, UsersListAdmin.class)).thenReturn(usersListAdmin);
        when(commonService.setResultObject(usersListAdmin, UsersListAdmin.class)).thenReturn(usersListAdmin);
        when(commonService.setResultModel(usersListAdmin, Constants.RESULT_STATUS_SUCCESS)).thenReturn(finalUsersListAdmin);

        UsersListAdmin resultList = (UsersListAdmin) usersService.getUsersAllByCluster(CLUSTER, USER_TYPE_AUTH_CLUSTER_ADMIN, SEARCH_NAME, LIMIT, OFFSET, ORDER_BY, ORDER);
    }

    @Test
    public void getUsersAllByCluster_User() {
        String reqUrlParam = "?userType=" + AUTH_USER + "&searchParam=" + SEARCH_NAME + "&orderBy=" + ORDER_BY + "&order=" + ORDER;

        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_CLUSTER.replace("{cluster:.+}", CLUSTER) + reqUrlParam , HttpMethod.GET, null, UsersListAdmin.class)).thenReturn(usersListAdmin);
        when(commonService.setResultObject(usersListAdmin, UsersListAdmin.class)).thenReturn(usersListAdmin);
        when(commonService.setResultModel(usersListAdmin, Constants.RESULT_STATUS_SUCCESS)).thenReturn(finalUsersListAdmin);

        UsersListAdmin resultList = (UsersListAdmin) usersService.getUsersAllByCluster(CLUSTER, USER_TYPE_AUTH_USER, SEARCH_NAME, LIMIT, OFFSET, ORDER_BY, ORDER);
    }


    @Test(expected = IllegalArgumentException.class)
    public void getUsersAllByCluster_No_User_Type() {
        when(usersService.getUsersAllByCluster(CLUSTER, USER_TYPE_AUTH_NONE, SEARCH_NAME, LIMIT, OFFSET, ORDER_BY, ORDER)).thenThrow(new IllegalArgumentException());
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> usersService.getUsersAllByCluster(CLUSTER, USER_TYPE_AUTH_NONE, SEARCH_NAME, LIMIT, OFFSET, ORDER_BY, ORDER));

        assertEquals(MessageConstant.USER_TYPE_ILLEGALARGUMENT, exception.getLocalizedMessage());
    }

    @Test
    public void getUsersListByNamespaceAdmin() {
        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE.replace("{cluster:.+}", CLUSTER).replace("{namespace:.+}", NAMESPACE) , HttpMethod.GET, null, UsersListAdmin.class)).thenReturn(usersListAdmin);

        UsersListAdmin resultList = usersService.getUsersListByNamespaceAdmin(CLUSTER, NAMESPACE);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    @Test
    public void getUsersListByNamespace() {
        when(restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE.replace("{cluster:.+}", CLUSTER).replace("{namespace:.+}", NAMESPACE) , HttpMethod.GET, null, UsersList.class)).thenReturn(usersList);

        UsersList resultList = usersService.getUsersListByNamespace(CLUSTER, NAMESPACE);
    }

    @Test
    public void getUsersInMultiNamespace() {
        Users usersByDefaultNamespace = new Users();

        usersByDefaultNamespace.setUserId(USER_ID);
        usersByDefaultNamespace.setServiceAccountName(SERVICE_ACCOUNT_NAME);
        usersByDefaultNamespace.setCreated(CREATED);
        usersByDefaultNamespace.setClusterName(CLUSTER);
        usersByDefaultNamespace.setClusterApiUrl(CLUSTER_API_URL);
        usersByDefaultNamespace.setClusterToken(CLUSTER_ADMIN_TOKEN);

        when(propertyService.getDefaultNamespace())
                .thenReturn(NAMESPACE);
        when(restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS
                .replace("{cluster:.+}", CLUSTER)
                .replace("{namespace:.+}", NAMESPACE)
                .replace("{userId:.+}", USER_ID), HttpMethod.GET, null, Users.class))
                .thenReturn(usersByDefaultNamespace);

        UsersList list = UsersModel.getResultUsersListWithClusterInfo();

        when(restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", USER_ID), HttpMethod.GET, null, UsersList.class))
                .thenReturn(list);
        when(propertyService.getIgnoreNamespaceList())
                .thenReturn(gIgnoreNamespaceList);
        when(propertyService.getCpMasterApiListSecretsGetUrl())
                .thenReturn("/api/v1/namespaces/{namespace}/secrets/{name}");
        when(restTemplateService.sendAdmin(TARGET_CP_MASTER_API, "/api/v1/namespaces/{namespace}/secrets/{name}".replace("{namespace}", list.getItems().get(0).getCpNamespace()).replace("{name}", list.getItems().get(0).getSaSecret()), HttpMethod.GET, null, Map.class))
                .thenReturn(gResultMap);

        UsersAdmin usersAdmin = new UsersAdmin();

        usersAdmin.setUserId(usersByDefaultNamespace.getUserId());
        usersAdmin.setServiceAccountName(usersByDefaultNamespace.getServiceAccountName());
        usersAdmin.setCreated(usersByDefaultNamespace.getCreated());
        usersAdmin.setClusterName(usersByDefaultNamespace.getClusterName());
        usersAdmin.setClusterApiUrl(usersByDefaultNamespace.getClusterApiUrl());
        usersAdmin.setClusterToken(usersByDefaultNamespace.getClusterToken());

        List<UsersAdmin.UsersDetails> usersDetailsList = new ArrayList<>();

        usersAdmin.setItems(usersDetailsList);

        when(commonService.userListProcessing(usersAdmin, OFFSET, LIMIT, "", "", "", UsersAdmin.class ))
                .thenReturn(usersAdmin);
        when(commonService.setResultObject(usersAdmin, UsersAdmin.class))
                .thenReturn(usersAdmin);
        when(commonService.setResultModel(usersAdmin, Constants.RESULT_STATUS_SUCCESS))
                .thenReturn(usersAdmin);

        UsersAdmin result = null;

        try {
            result = (UsersAdmin) usersService.getUsersInMultiNamespace(CLUSTER, USER_ID, LIMIT, OFFSET);
        }
        catch(Exception e) {
            result = new UsersAdmin();
        }

        assertThat(result).isNotNull();
        assertEquals(usersAdmin.getUserId(), result.getUserId());
        assertEquals(usersAdmin.getServiceAccountName(), result.getServiceAccountName());
        assertEquals(usersAdmin.getCreated(), result.getCreated());
        assertEquals(usersAdmin.getClusterName(), result.getClusterName());
        assertEquals(usersAdmin.getClusterApiUrl(), result.getClusterApiUrl());
        assertEquals(usersAdmin.getClusterToken(), result.getClusterToken());
    }

    @Test(expected = Exception.class)
    public void getUsersInMultiNamespace_Not_Found_Result_Status() {
        when(propertyService.getDefaultNamespace())
                .thenReturn(NAMESPACE);
        when(restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS
                .replace("{cluster:.+}", CLUSTER)
                .replace("{namespace:.+}", NAMESPACE)
                .replace("{userId:.+}", USER_ID), HttpMethod.GET, null, Users.class))
                .thenThrow(new Exception());

        ResultStatus result = null;

        try {
            result = (ResultStatus) usersService.getUsersInMultiNamespace(CLUSTER, USER_ID, LIMIT, OFFSET);
        }
        catch(Exception e) {
        }

        assertEquals(Constants.NOT_FOUND_RESULT_STATUS, result);
    }

    @Test
    public void getUsersNameListByNamespace() {
        when(restTemplateService.send(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_NAMES_LIST
                .replace("{cluster:.+}", CLUSTER)
                .replace("{namespace:.+}", NAMESPACE), HttpMethod.GET, null, Map.class)).thenReturn(gResultNamesMap);

        Map<String, List> resultMap = usersService.getUsersNameListByNamespace(CLUSTER, NAMESPACE);
        assertEquals(resultMap.get("users"), gResultNamesMap.get("users"));
    }

    @Test
    public void getUsersDetailsForLogin() {
        Users gFinalResultModelForUser = new Users();
        gFinalResultModelForUser.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultModelForUser.setResultMessage(Constants.RESULT_STATUS_SUCCESS);

        when(restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DETAIL_LOGIN.replace("{userId:.+}", USER_ID) + "?isAdmin=" + isAdminString, HttpMethod.GET, null, Users.class)).thenReturn(gFinalResultModelForUser);

        Users result = usersService.getUsersDetailsForLogin(USER_ID, isAdminString);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    @Test
    public void getUsersByNamespaceAndNsAdmin() {
        when(restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_BY_NAMESPACE_NS_ADMIN.replace("{cluster:.+}", CLUSTER).replace("{namespace:.+}", NAMESPACE), HttpMethod.GET, null, Users.class)).thenReturn(users);

        Users result = usersService.getUsersByNamespaceAndNsAdmin(CLUSTER, NAMESPACE);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    @Test
    public void getUsersDetails() {
        UsersList usersList = UsersModel.getResultUsersListWithClusterInfo();
        usersList.setResultCode(RESULT_STATUS_SUCCESS);
        when(restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", USER_ID), HttpMethod.GET, null, UsersList.class)).thenReturn(usersList);

        UsersList resultList = usersService.getUsersDetails(USER_ID);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    @Test
    public void getUsers() {
        when(restTemplateService.send(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS.replace("{cluster:.+}", CLUSTER).replace("{namespace:.+}", NAMESPACE).replace("{userId:.+}", USER_ID), HttpMethod.GET, null, Users.class)).thenReturn(users);
        when(commonService.setResultModel(users, Constants.RESULT_STATUS_SUCCESS)).thenReturn(users);

        Users result = usersService.getUsers(CLUSTER, NAMESPACE, USER_ID);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    @Test
    public void createUsers() {
        Users users = UsersModel.getResultUser();
        when(restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class)).thenReturn(gResultStatusModel);

        ResultStatus resultStatus = usersService.createUsers(users);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultStatus.getResultCode());
    }

    @Test
    public void updateUsers() {
        Users users = UsersModel.getResultUser();
        when(restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.PUT, users, ResultStatus.class)).thenReturn(gResultStatusModel);

        ResultStatus resultStatus = usersService.updateUsers(users);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultStatus.getResultCode());
    }

    @Test
    public void registerUsers() {
        Users users = new Users();

        List<Users.NamespaceRole> list = new ArrayList<>();

        Users.NamespaceRole role = new Users.NamespaceRole();

        role.setRole(ROLE);
        role.setNamespace(NAMESPACE);

        list.add(role);

        users.setSelectValues(list);
        users.setUserId(USER_ID);
        users.setUserType(Constants.AUTH_USER);
        users.setCpNamespace(NAMESPACE);
        users.setServiceAccountName(users.getUserId());
        users.setRoleSetCode(ROLE);
        users.setIsActive(CHECK_Y);

        when(resourceYamlService.createServiceAccount(USER_ID, NAMESPACE))
                .thenReturn(gResultStatusModel);
        when(resourceYamlService.createRoleBinding(USER_ID, NAMESPACE, ROLE))
                .thenReturn(gResultStatusModel);
        when(restTemplateService.getSecretName(NAMESPACE, users.getUserId()))
                .thenReturn(SECRET_NAME);
        users.setSaSecret(SECRET_NAME);

        AccessToken accessToken = new AccessToken();
        accessToken.setUserAccessToken(SECRET_TOKEN);

        when(accessTokenService.getSecrets(NAMESPACE, SECRET_NAME))
                .thenReturn(accessToken);
        users.setSaToken(SECRET_TOKEN);

        Clusters clusters = new Clusters();

        clusters.setClusterApiUrl("API_URL");
        clusters.setClusterName("NAME");
        clusters.setClusterToken("TOKEN");

        when(clustersService.getClusters(CLUSTER))
                .thenReturn(clusters);
        when(restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class))
                .thenReturn(gResultStatusModel);
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class))
                .thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS))
                .thenReturn(gResultStatusModel);

        ResultStatus resultStatus = (ResultStatus) usersService.registerUsers(users);

        assertThat(resultStatus).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultStatus.getResultCode());

        // Fail
        when(restTemplateService.sendAdmin(TARGET_COMMON_API, "/users", HttpMethod.POST, users, ResultStatus.class))
                .thenReturn(gResultFailStatusModel);
        when(propertyService.getCpMasterApiListUsersDeleteUrl())
                .thenReturn("/api/v1/namespaces/{namespace}/serviceaccounts/{name}");
        when(restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListUsersDeleteUrl().replace("{namespace}", NAMESPACE).replace("{name}", users.getUserId()), HttpMethod.DELETE, null, Object.class, true))
                .thenReturn(new Object());

        ResultStatus resultFailStatus = (ResultStatus) usersService.registerUsers(users);

        assertThat(resultFailStatus).isNotNull();
        assertEquals(Constants.RESULT_STATUS_FAIL, resultFailStatus.getResultCode());
    }

//    @Test
//    public void modifyUsersAdmin() throws Exception {
////        //getUsersInMultiNamespace();
////        //when(propertyService.getDefaultNamespace()).thenReturn(NAMESPACE);
////        //doReturn(NAMESPACE).when(propertyService).getDefaultNamespace();
////        //doReturn(UsersModel.getUsersAdminList()).when(usersService).getUsersInMultiNamespace(CLUSTER, USER_ID, LIMIT, OFFSET);
////
////        //when(usersService.getUsersInMultiNamespace(CLUSTER, USER_ID, LIMIT, OFFSET)).thenReturn(UsersModel.getUsersAdminList());
////        List<UsersAdmin.UsersDetails> usersDetails = UsersModel.getUsersAdminList().getItems();
////        //when(usersService.getUsersInMultiNamespace(CLUSTER, USER_ID, LIMIT, OFFSET)).thenReturn(usersDetails);
//
//
//
//        resourceYamlService.createServiceAccount("paasta", "addInNamespace");
//        resourceYamlService.createRoleBinding("paasta", "addInNamespace", "ns-init-role");
//
//        ResultStatus resultStatus = usersService.modifyUsersAdmin(CLUSTER, USER_ID, UsersModel.getResultUser());
//    }

    @Test
    public void deleteUsers() {
        when(propertyService.getCpMasterApiListUsersDeleteUrl()).thenReturn("/api/v1/namespaces/{namespace}/serviceaccounts/{name}");
        when(restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/serviceaccounts/" + USER_ID, HttpMethod.DELETE, null, Object.class, isAdmin)).thenReturn(gResultStatusModel);

        when(propertyService.getCpMasterApiListRoleBindingsDeleteUrl()).thenReturn("/apis/rbac.authorization.k8s.io/v1/namespaces/{namespace}/rolebindings/{name}");
        when(restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, "/apis/rbac.authorization.k8s.io/v1/namespaces/" + NAMESPACE + "/rolebindings/" + USER_ID + Constants.NULL_REPLACE_TEXT + ROLE + "-binding", HttpMethod.DELETE, null, Object.class, isAdmin)).thenReturn(gResultStatusModel);

        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USER_DELETE + UsersModel.getResultUser().getId(), HttpMethod.DELETE, null, Object.class)).thenReturn(gResultStatusModel);

        ResultStatus resultStatus = usersService.deleteUsers(UsersModel.getResultUser());
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultStatus.getResultCode());
    }

    @Test
    public void modifyUsers() {
        when(restTemplateService.sendAdmin(TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_DETAIL.replace("{userId:.+}", USER_ID), HttpMethod.PUT, UsersModel.getResultUser(), ResultStatus.class)).thenReturn(gResultStatusModel);

        ResultStatus resultStatus = usersService.modifyUsers(USER_ID, UsersModel.getResultUser());
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultStatus.getResultCode());
    }

    @Test
    public void deleteUsersByAllNamespaces() {
        UsersList usersList = UsersModel.getResultUsersListWithClusterInfo();
        usersList.setResultCode(RESULT_STATUS_SUCCESS);

        ResultStatus finalRs = new ResultStatus();
        finalRs.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        finalRs.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        finalRs.setHttpStatusCode(CommonStatusCode.OK.getCode());
        finalRs.setDetailMessage(CommonStatusCode.OK.getMsg());
        finalRs.setNextActionUrl(Constants.URI_USERS);

        when(usersService.getUsersDetails(USER_ID)).thenReturn(usersList);

        for (Users users : usersList.getItems()) {
            deleteUsers();
        }
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_USERS)).thenReturn(finalRs);

        ResultStatus resultStatus = usersService.deleteUsersByAllNamespaces(USER_ID);
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultStatus.getResultCode());
    }

    @Test
    public void modifyUsersConfig() throws NoSuchMethodException {
        AccessToken gAccessTokenModel = new AccessToken();
        gAccessTokenModel.setCaCertToken("");
        gAccessTokenModel.setUserAccessToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IktNWmgxVXB3ajgwS0NxZjFWaVZJVGVvTXJoWnZ5dG0tMGExdzNGZjBKX00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ0ZW1wLW5hbWVzcGFjZSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJ0ZXN0LXRva2VuLWpxcng0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InRlc3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI3Y2Q0Nzk4OC01YWViLTQ1ODQtYmNmOS04OTkwZTUzNWEzZGIiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6dGVtcC1uYW1lc3BhY2U6dGVzdCJ9.ZEwhnscTtPW6WrQ5I7fFWcsLWEqnilw7I8i7C4aSXElFHd583OQqTYGk8RUJU7UM6b2T8oKstejkLWE9xP3TchYyG5T-omZBCMe00JZIof4tp0MRZLgBhXizYXGvLb2bcMdlcWg2bCCVRO92Hjik-r-vqfaGbsRGx4dT2dk1sI4RA-XDnMsVFJS94V9P58cBupT1gRMrwWStrqlXrbiwgfIlGbU9GXnA07JUCMy-1wUYdMmRaICdj-Q7eNZ5BmKCNsFBcJKaDl5diNw-gSka2F61sywpezU-30sWAtRHYIYZt6PaAaZ4caAdR8f43Yq1m142RWsr3tunLgQ768UNtQ");
        gAccessTokenModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gAccessTokenModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gAccessTokenModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gAccessTokenModel.setDetailMessage(CommonStatusCode.OK.getMsg());

        List<Users> usersArrayList = new ArrayList<>();
        Users usersUpdateRole = modifyUsers;
        usersUpdateRole.setRoleSetCode(ADMIN_ROLE);

        usersArrayList.add(usersUpdateRole);

        ResultStatus finalRs = new ResultStatus();
        finalRs.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        finalRs.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        finalRs.setHttpStatusCode(CommonStatusCode.OK.getCode());
        finalRs.setDetailMessage(CommonStatusCode.OK.getMsg());

        when(usersService.getUsersListByNamespace(CLUSTER, NAMESPACE)).thenReturn(modifyUsersList);
        when(usersService.getUsers(CLUSTER, NAMESPACE, USER_ID)).thenReturn(users);
        when(propertyService.getCpMasterApiListRoleBindingsDeleteUrl()).thenReturn("/apis/rbac.authorization.k8s.io/v1/namespaces/{namespace}/rolebindings/{name}");
        when(restTemplateService.sendYaml(TARGET_CP_MASTER_API, propertyService.getCpMasterApiListRoleBindingsDeleteUrl().replace("{namespace}", NAMESPACE).replace("{name}", usersUpdateRole.getServiceAccountName() + Constants.NULL_REPLACE_TEXT + usersUpdateRole.getRoleSetCode() + "-binding"), HttpMethod.DELETE, null, Object.class, true)).thenReturn(gResultStatusModel);

        when(resourceYamlService.createRoleBinding(USER_ID, NAMESPACE, ADMIN_ROLE)).thenReturn(finalRs);
        doReturn(SECRET_NAME).when(restTemplateService).getSecretName(NAMESPACE, USER_ID);
        when(accessTokenService.getSecrets(NAMESPACE, SECRET_NAME)).thenReturn(gAccessTokenModel);

        ReflectionTestUtils.invokeMethod(usersService, "updateSetRoleUser", NAMESPACE, USER_ID, ADMIN_ROLE, usersUpdateRole);

        usersService.modifyUsersConfig(CLUSTER, NAMESPACE, usersArrayList);
    }

    @Test
    public void getUsersListInNamespaceAdmin() {
        UsersListInNamespaceAdmin listInNamespaceAdmin = new UsersListInNamespaceAdmin();
        UsersListInNamespaceAdmin.UserDetail detail = new UsersListInNamespaceAdmin.UserDetail();
        List<UsersListInNamespaceAdmin.UserDetail> detailList = new ArrayList<>();
        detail.setIsAdmin("Y");
        detail.setUserId("paasta");
        detail.setServiceAccountName("paasta");
        detail.setCreated("2020-11-03");
        detail.setUserType("NAMESPACE_ADMIN");

        detailList.add(detail);

        listInNamespaceAdmin.setItems(detailList);
        listInNamespaceAdmin.setResultCode(RESULT_STATUS_SUCCESS);

        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_USERS_LIST_BY_NAMESPACE.replace("{cluster:.+}", CLUSTER).replace("{namespace:.+}", NAMESPACE), HttpMethod.GET, null, UsersListInNamespaceAdmin.class)).thenReturn(listInNamespaceAdmin);

        when(commonService.setResultObject(gResultStatusModel, UsersListInNamespaceAdmin.class)).thenReturn(listInNamespaceAdmin);
        when(commonService.setResultModel(listInNamespaceAdmin, Constants.RESULT_STATUS_SUCCESS)).thenReturn(listInNamespaceAdmin);

        usersService.getUsersListInNamespaceAdmin(CLUSTER, NAMESPACE, OFFSET, LIMIT,ORDER_BY,ORDER,SEARCH_NAME);
    }

    @Test
    public void commonSaveClusterInfo() {
        Clusters gResultClusterModel = new Clusters();
        gResultClusterModel.setClusterApiUrl(CLUSTER_API_URL);
        gResultClusterModel.setClusterName(CLUSTER);
        gResultClusterModel.setClusterToken(CLUSTER_ADMIN_TOKEN);

        when(clustersService.getClusters(CLUSTER)).thenReturn(gResultClusterModel);

        Users users = usersService.commonSaveClusterInfo(CLUSTER, UsersModel.getResultUser());
        assertEquals(users, UsersModel.getResultUserWithClusterInfo());
    }

    @Test
    public void getUsersNameListByNamespaceAdmin_All_Namespace() {
        UsersInNamespace usersInNamespace = new UsersInNamespace();
        usersInNamespace.setNamespace(ALL_NAMESPACES);

        List<String> names = new ArrayList<>();
        names.add("test");
        names.add("paasta");

        UsersInNamespace finalUsersInNamespace = usersInNamespace;
        finalUsersInNamespace.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        finalUsersInNamespace.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        finalUsersInNamespace.setHttpStatusCode(CommonStatusCode.OK.getCode());
        finalUsersInNamespace.setDetailMessage(CommonStatusCode.OK.getMsg());

        when(restTemplateService.send(TARGET_COMMON_API, "/users/names", HttpMethod.GET, null, Map.class)).thenReturn(gResultNamesMap);
        when(commonService.setResultModel(usersInNamespace, Constants.RESULT_STATUS_SUCCESS)).thenReturn(finalUsersInNamespace);

        usersService.getUsersNameListByNamespaceAdmin(CLUSTER, ALL_NAMESPACES);
    }

    @Test
    public void getUsersNameListByNamespaceAdmin_Namespace() {
        UsersInNamespace usersInNamespace = new UsersInNamespace();
        usersInNamespace.setNamespace(NAMESPACE);

        List<String> names = new ArrayList<>();
        names.add("test");
        names.add("paasta");

        UsersInNamespace finalUsersInNamespace = usersInNamespace;
        finalUsersInNamespace.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        finalUsersInNamespace.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        finalUsersInNamespace.setHttpStatusCode(CommonStatusCode.OK.getCode());
        finalUsersInNamespace.setDetailMessage(CommonStatusCode.OK.getMsg());

        when(usersService.getUsersByNamespaceAndNsAdmin(CLUSTER, NAMESPACE)).thenReturn(users);

        when(restTemplateService.send(TARGET_COMMON_API, "/users/names", HttpMethod.GET, null, Map.class)).thenReturn(gResultNamesMap);
        when(commonService.setResultModel(usersInNamespace, Constants.RESULT_STATUS_SUCCESS)).thenReturn(finalUsersInNamespace);

        usersService.getUsersNameListByNamespaceAdmin(CLUSTER, NAMESPACE);
    }
}
