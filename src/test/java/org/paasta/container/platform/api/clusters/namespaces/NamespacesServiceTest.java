package org.paasta.container.platform.api.clusters.namespaces;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.accessInfo.AccessToken;
import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasList;
import org.paasta.container.platform.api.clusters.resourceQuotas.ResourceQuotasService;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.CommonResourcesYaml;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class NamespacesServiceTest {

    private static final String CLUSTER = "cp-cluster";
    private static final String NAMESPACE = "test-namespace";
    private static final String USER_ID = "test-user";
    private static final String YAML_STRING = "test-yaml-string";
    private static final String FIELD_SELECTOR = "?fieldSelector=metadata.name!=kubernetes-dashboard,metadata.name!=kube-node-lease,metadata.name!=kube-public,metadata.name!=kube-system,metadata.name!=temp-namespace";

    private static final int OFFSET = 0;
    private static final int LIMIT = 0;
    private static final String ORDER_BY = "creationTime";
    private static final String ORDER = "desc";
    private static final String SEARCH_NAME = "";
    private static final boolean isAdmin = true;
    private static final boolean isNotAdmin = false;

    public static final String USERS = "users";
    public static final String NAMESPACE_ADMIN_USER_ID = "";

    private static HashMap gResultMap = null;
    private static HashMap gResultAdminMap = null;
    private static HashMap gResultAdminFailMap = null;


    private static List<String> gResultList;
    private static Namespaces gResultModel = null;
    private static Namespaces gFinalResultModel = null;

    private static NamespacesList gResultListModel = null;
    private static NamespacesList gFinalResultListModel = null;
    private static NamespacesList gFinalResultListFailModel = null;

    private static ResourceQuotasList gResultListRqModel = null;
    private static ResourceQuotasList gFinalResultListRqModel = null;
    private static ResourceQuotasList gFinalResultListRqFailModel = null;

    private static NamespacesAdmin gResultAdminModel = null;
    private static NamespacesAdmin gFinalResultAdminModel = null;
    private static NamespacesAdmin gFinalResultAdminFailModel = null;

    private static NamespacesListAdmin gResultListAdminModel = null;
    private static NamespacesListAdmin gFinalResultListAdminModel = null;
    private static NamespacesListAdmin gFinalResultListAdminFailModel = null;


    private static CommonResourcesYaml gResultYamlModel = null;
    private static CommonResourcesYaml gFinalResultYamlModel = null;

    private static ResultStatus gResultStatusModel = null;
    private static ResultStatus gResultFailModel = null;
    private static ResultStatus gFinalResultStatusModel = null;


    private static ResourceQuotasList gResourceQuotasListModel = null;
    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @Mock
    UsersService usersService;

    @Mock
    AccessTokenService accessTokenService;

    @Mock
    ResourceQuotasService resourceQuotasService;

    @Mock
    ResourceYamlService resourceYamlService;

    @InjectMocks
    NamespacesService namespacesService;

    @Before
    public void setUp() throws Exception {
        gResultMap = new HashMap();


        gResultStatusModel = new ResultStatus();
        gResultStatusModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gResultStatusModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gResultStatusModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gResultStatusModel.setDetailMessage(CommonStatusCode.OK.getMsg());

        gFinalResultStatusModel = new ResultStatus();
        gFinalResultStatusModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultStatusModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultStatusModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gFinalResultStatusModel.setDetailMessage(CommonStatusCode.OK.getMsg());
        gFinalResultStatusModel.setNextActionUrl(Constants.URI_WORKLOAD_DEPLOYMENTS);

        // 리스트가져옴
        gResultListModel = new NamespacesList();

        gFinalResultListModel = new NamespacesList();
        gFinalResultListModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gFinalResultListFailModel = new NamespacesList();
        gFinalResultListFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);

        // 하나만 가져옴
        gResultModel = new Namespaces();
        gFinalResultModel = new Namespaces();
        gFinalResultModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gResultFailModel = new ResultStatus();
        gResultFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);
        gResultFailModel.setResultMessage(Constants.RESULT_STATUS_FAIL);
        gResultFailModel.setHttpStatusCode(CommonStatusCode.NOT_FOUND.getCode());
        gResultFailModel.setDetailMessage(CommonStatusCode.NOT_FOUND.getMsg());

        gResultYamlModel = new CommonResourcesYaml();
        gFinalResultYamlModel = new CommonResourcesYaml();
        gFinalResultYamlModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultYamlModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultYamlModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gFinalResultYamlModel.setDetailMessage(CommonStatusCode.OK.getMsg());
        gFinalResultYamlModel.setSourceTypeYaml(YAML_STRING);

        // 리스트가져옴
        gResultAdminMap = new HashMap();
        gResultListAdminModel = new NamespacesListAdmin();
        gFinalResultListAdminModel = new NamespacesListAdmin();

        gFinalResultListAdminModel = new NamespacesListAdmin();
        gFinalResultListAdminModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultListAdminModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultListAdminModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gFinalResultListAdminModel.setDetailMessage(CommonStatusCode.OK.getMsg());

        gFinalResultListAdminFailModel = new NamespacesListAdmin();
        gFinalResultListAdminFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);
        gFinalResultListAdminFailModel.setResultMessage(Constants.RESULT_STATUS_FAIL);
        gFinalResultListAdminFailModel.setHttpStatusCode(CommonStatusCode.NOT_FOUND.getCode());
        gFinalResultListAdminFailModel.setDetailMessage(CommonStatusCode.NOT_FOUND.getMsg());


        // 하나만 가져옴
        gResultAdminModel = new NamespacesAdmin();
        gFinalResultAdminModel = new NamespacesAdmin();
        gFinalResultAdminModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultAdminModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultAdminModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gFinalResultAdminModel.setDetailMessage(CommonStatusCode.OK.getMsg());

        gFinalResultAdminFailModel = new NamespacesAdmin();
        gFinalResultAdminFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);
        gFinalResultAdminFailModel.setResultMessage(Constants.RESULT_STATUS_FAIL);
        gFinalResultAdminFailModel.setHttpStatusCode(CommonStatusCode.NOT_FOUND.getCode());
        gFinalResultAdminFailModel.setDetailMessage(CommonStatusCode.NOT_FOUND.getMsg());

        gResourceQuotasListModel = new ResourceQuotasList();
        gFinalResultListRqModel = new ResourceQuotasList();
        gFinalResultListRqFailModel = new ResourceQuotasList();
    }

    /**
     * Namespaces 상세 조회(Get Namespaces detail) Test
     */
    @Test
    public void getNamespaces_Valid_ReturnModel() {

        //when
        when(propertyService.getCpMasterApiListNamespacesGetUrl()).thenReturn("/api/v1/namespaces/{namespace}");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, Namespaces.class)).thenReturn(gResultModel);
        when(commonService.setResultModel(gResultModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultModel);

        //call method
        Namespaces result = namespacesService.getNamespaces(NAMESPACE);

        //compare result
        assertThat(result).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());

    }

    /**
     * NameSpaces 상세 조회(Get NameSpaces Admin detail) Test
     */
    @Test
    public void getNamespacesAdmin_Valid_ReturnModel() {

        //when
        when(propertyService.getCpMasterApiListNamespacesGetUrl()).thenReturn("/api/v1/namespaces/{namespace}");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, NamespacesAdmin.class)).thenReturn(gResultAdminModel);
        when(commonService.setResultModel(gResultAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultAdminModel);

        //call method
        NamespacesAdmin result = (NamespacesAdmin) namespacesService.getNamespacesAdmin(NAMESPACE);

        //when
        //compare result
        assertThat(result).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());

    }

    /**
     * NameSpaces Admin 목록 조회(Get NameSpaces Admin list) Test
     */
    @Test
    public void getNamespacesListAdmin_Valid_ReturnModel() {

        //when
        when(propertyService.getCpMasterApiListNamespacesListUrl()).thenReturn("/api/v1/namespaces");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces"+
                commonService.generateFieldSelectorForExceptNamespace(Constants.RESOURCE_CLUSTER), HttpMethod.GET, null, Map.class)).thenReturn(gResultAdminMap);

        when(commonService.setResultObject(gResultAdminMap, NamespacesListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.resourceListProcessing(gResultListAdminModel, OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME, NamespacesListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.setResultModel(gResultListAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListAdminModel);

        //call method
        NamespacesListAdmin resultList = (NamespacesListAdmin) namespacesService.getNamespacesListAdmin(OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME);

        //compare result
        assertThat(resultList).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());

    }

    /**
     * NameSpaces Admin YAML 조회(Get NameSpaces yaml) Test
     */
    @Test
    public void getNamespacesAdminYaml_Valid_ReturnModel() {

        //when
        when(propertyService.getCpMasterApiListNamespacesGetUrl()).thenReturn("/api/v1/namespaces/{namespace}");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/{namespace}"
                + NAMESPACE , HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML)).thenReturn(YAML_STRING);

        when(commonService.setResultObject(gResultMap, CommonResourcesYaml.class)).thenReturn(gResultYamlModel);
        when(commonService.setResultModel(gResultYamlModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultYamlModel);

        //call method
        CommonResourcesYaml result = (CommonResourcesYaml) namespacesService.getNamespacesAdminYaml(NAMESPACE, gResultMap);

        //compare result
        assertEquals(YAML_STRING, result.getSourceTypeYaml());
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * NameSpaces 삭제(Delete NameSpaces) Test
     */
    @Test
    public void deleteNamespaces_Valid_ReturnModel() {

        Users users = new Users();
        users.setId(0);
        users.setUserId("paasta");
        users.setServiceAccountName("paasta");
        users.setRoleSetCode("paas-ta-container-platform-init-role");
        users.setIsActive("Y");
        users.setDescription("aaaa");
        users.setEmail("paasta@gmail.com");
        users.setPassword("paasta");
        users.setCpNamespace("cp-namespace");
        users.setSaSecret("paasta-token-jqrx4");
        users.setSaToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IktNWmgxVXB3ajgwS0NxZjFWaVZJVGVvTXJoWnZ5dG0tMGExdzNGZjBKX00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJ0ZW1wLW5hbWVzcGFjZSIsImt1YmVybmV0ZXMuaW8vc2VydmljZWFjY291bnQvc2VjcmV0Lm5hbWUiOiJ0ZXN0LXRva2VuLWpxcng0Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InRlc3QiLCJrdWJlcm5ldGVzLmlvL3NlcnZpY2VhY2NvdW50L3NlcnZpY2UtYWNjb3VudC51aWQiOiI3Y2Q0Nzk4OC01YWViLTQ1ODQtYmNmOS04OTkwZTUzNWEzZGIiLCJzdWIiOiJzeXN0ZW06c2VydmljZWFjY291bnQ6dGVtcC1uYW1lc3BhY2U6dGVzdCJ9.ZEwhnscTtPW6WrQ5I7fFWcsLWEqnilw7I8i7C4aSXElFHd583OQqTYGk8RUJU7UM6b2T8oKstejkLWE9xP3TchYyG5T-omZBCMe00JZIof4tp0MRZLgBhXizYXGvLb2bcMdlcWg2bCCVRO92Hjik-r-vqfaGbsRGx4dT2dk1sI4RA-XDnMsVFJS94V9P58cBupT1gRMrwWStrqlXrbiwgfIlGbU9GXnA07JUCMy-1wUYdMmRaICdj-Q7eNZ5BmKCNsFBcJKaDl5diNw-gSka2F61sywpezU-30sWAtRHYIYZt6PaAaZ4caAdR8f43Yq1m142RWsr3tunLgQ768UNtQ");
        users.setUserType("CLUSTER_ADMIN");
        users.setCreated("2020-10-13");
        users.setLastModified("2020-10-13");
        users.setClusterApiUrl("111.111.111.111:6443");
        users.setClusterToken("eyJhbGciOiJSUzI1NiIsImtpZCI6IktNWmgxVXB3ajgwS0NxZjFWaVZJVGVvTXJoWnZ5dG0tMGExdzNGZjBKX00ifQ.eyJpc3MiOiJrdWJlcm5ldGVzL3NlcnZpY2VhY2NvdW50Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9uYW1lc3BhY2UiOiJwYWFzLWYxMGU3ZTg4LTQ4YTUtNGUyYy04Yjk5LTZhYmIzY2ZjN2Y2Zi1jYWFzIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZWNyZXQubmFtZSI6InN1cGVyLWFkbWluLXRva2VuLWtzbXo1Iiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQubmFtZSI6InN1cGVyLWFkbWluIiwia3ViZXJuZXRlcy5pby9zZXJ2aWNlYWNjb3VudC9zZXJ2aWNlLWFjY291bnQudWlkIjoiMjMwZWQ1OGQtNzc0MC00MDI4LTk0MTEtYTM1MzVhMWM0NjU4Iiwic3ViIjoic3lzdGVtOnNlcnZpY2VhY2NvdW50OnBhYXMtZjEwZTdlODgtNDhhNS00ZTJjLThiOTktNmFiYjNjZmM3ZjZmLWNhYXM6c3VwZXItYWRtaW4ifQ.nxnIJCOH_XVMK71s0gF8bgzSxA7g6_y7hGdboLvSqIAGf9J9AgG1DouP29uShK19fMsl9IdbGODPvtuiBz4QyGLPARZldmlzEyFG3k08UMNay1xX_oK-Fe7atMlYgvoGzyM_5-Zp5dyvnxE2skk524htMGHqW1ZwnHLVxtBg8AuGfMwLW1xahmktsNZDG7pRMasPsj73E85lfavMobBlcs4hwVcZU82gAg0SK1QVe7-Uc2ip_9doNo6_9rGW3FwHdVgUNAeCvPRGV0W1dKJv0IX5e_7fIPIznj2xXcZoHf3BnKfDayDIKJOCdsEsy_2NGi1tiD3UvzDDzZpz02T2sg");
        users.setClusterName("cp-cluster");

        when(propertyService.getCpMasterApiListNamespacesDeleteUrl()).thenReturn("/api/v1/namespaces/{name}");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE,  HttpMethod.DELETE, null, ResultStatus.class)).thenReturn(gResultStatusModel);

        Map<String, List> userNameList = new HashMap<>();
        List<String> aaa = new ArrayList<>();
        aaa.add("paasta");
        aaa.add("test");

        userNameList.put("users", aaa);

        when(usersService.getUsersNameListByNamespace(CLUSTER, NAMESPACE)).thenReturn(userNameList);

        when(usersService.getUsers(CLUSTER, NAMESPACE, USER_ID)).thenReturn(users);
        when(usersService.deleteUsers(users)).thenReturn(gResultStatusModel);

        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_CLUSTER_NAMESPACES)).thenReturn(gFinalResultStatusModel);

        ResultStatus rs = namespacesService.deleteNamespaces(CLUSTER, NAMESPACE);

    }

    /**
     * Namespaces 생성(Create Namespaces) Test
     */
    @Test
    public void createInitNamespaces_Valid_ReturnModel() {
        // given
        List<String> resourceQuotasList = new ArrayList<>();
        resourceQuotasList.add("cp-resource-quotas-low");

        List<String> limitRangesList = new ArrayList<>();
        limitRangesList.add("cp-limit-ranges-low");

        NamespacesInitTemplate nsInitTemp = new NamespacesInitTemplate();
        nsInitTemp.setName(NAMESPACE);
        nsInitTemp.setNsAdminUserId(NAMESPACE_ADMIN_USER_ID);
        nsInitTemp.setResourceQuotasList(resourceQuotasList);
        nsInitTemp.setLimitRangesList(limitRangesList);

        when(resourceYamlService.createNamespace(NAMESPACE)).thenReturn(gFinalResultStatusModel);
        resourceYamlService.createInitRole(NAMESPACE);
        resourceYamlService.createNsAdminRole(NAMESPACE);
        when(resourceYamlService.createServiceAccount(NAMESPACE_ADMIN_USER_ID, NAMESPACE)).thenReturn(gFinalResultStatusModel);
        when(propertyService.getAdminRole()).thenReturn("paas-ta-container-platform-admin-role");
        when(resourceYamlService.createRoleBinding(NAMESPACE_ADMIN_USER_ID, NAMESPACE, "paas-ta-container-platform-admin-role")).thenReturn(gFinalResultStatusModel);

        // for - if
        String rq = "paas-ta-container-platform-low-rq";
        resourceYamlService.createDefaultResourceQuota(NAMESPACE, rq);

        // for - if
        String lr = "paas-ta-container-platform-low-limit-range";
        resourceYamlService.createDefaultLimitRanges(NAMESPACE, lr);

        String saSecretName = "cp-secret";
        when(restTemplateService.getSecretName(NAMESPACE, NAMESPACE_ADMIN_USER_ID)).thenReturn(saSecretName);

        Users newNsUser = new Users();
        newNsUser.setId(0);
        newNsUser.setCpNamespace(NAMESPACE);
        when(propertyService.getAdminRole()).thenReturn("paas-ta-container-platform-admin-role");
        newNsUser.setRoleSetCode("paas-ta-container-platform-admin-role");
        newNsUser.setSaSecret(saSecretName);
        AccessToken accessToken = new AccessToken();
        accessToken.setUserAccessToken("");
        when(accessTokenService.getSecrets(NAMESPACE, saSecretName)).thenReturn(accessToken);
        newNsUser.setSaToken(accessToken.getUserAccessToken());
        newNsUser.setUserType(Constants.AUTH_NAMESPACE_ADMIN);
        newNsUser.setIsActive(Constants.CHECK_Y);
        when(propertyService.getDefaultNamespace()).thenReturn("paas-ta-container-platform-temp-namespace");
        when(usersService.getUsers(CLUSTER, "paas-ta-container-platform-temp-namespace", NAMESPACE_ADMIN_USER_ID)).thenReturn(newNsUser);
        when(usersService.createUsers(usersService.commonSaveClusterInfo(Constants.SINGLE_CLUSTER_NAME, newNsUser))).thenReturn(gResultStatusModel);
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, "YOUR_NAMESPACES_LIST_PAGE")).thenReturn(gFinalResultStatusModel);

        // when
        ResultStatus result = namespacesService.createInitNamespaces(CLUSTER, nsInitTemp);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Namespaces 수정(modify Namespaces) Test
     */
    @Test
    public void modifyInitNamespaces_Valid_ReturnModel() {



    }

    /**
     * ResourceQuotas 변경 Test
     */
    @Test
    public void modifyResourceQuotas_Valid_ReturnModel() {

    }

    /**
     * LimitRanges 변경 Test
     */
    @Test
    public void modifyLimitRanges_Valid_ReturnModel() {

    }

    /**
     * Namespace Selectbox를 위한 전체 목록 조회
     */
    @Test
    public void getNamespacesListForSelectbox_Valid_ReturnModel() {

    }

}