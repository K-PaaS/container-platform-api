package org.paasta.container.platform.api.workloads.pods;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.*;
import org.paasta.container.platform.api.common.model.CommonResourcesYaml;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class PodsServiceTest {
    private static final String CLUSTER = "cp-cluster";
    private static final String NAMESPACE = "cp-namespace";
    private static final String PODS_NAME = "test-pod";
    private static final String YAML_STRING = "test-yaml-string";

    private static final String SELECTOR = "test-selector";
    private static final String NODE_NAME = "test-node";
    private static final String FIELD_SELECTOR = "?fieldSelector=metadata.namespace!=kubernetes-dashboard,metadata.namespace!=kube-node-lease,metadata.namespace!=kube-public,metadata.namespace!=kube-system,metadata.namespace!=temp-namespace";

    private static final int OFFSET = 0;
    private static final int LIMIT = 0;
    private static final String ORDER_BY = "creationTime";
    private static final String ORDER = "desc";
    private static final String SEARCH_NAME = "";
    private static final boolean isAdmin = true;
    private static final boolean isNotAdmin = false;

    private static HashMap gResultMap = null;

    private static PodsList gResultListModel = null;
    private static PodsList gFinalResultListModel = null;

    private static PodsListAdmin gResultListAdminModel = null;
    private static PodsListAdmin gFinalResultListAdminModel = null;

    private static PodsListAdminList gResultListAdminListModel = null;
    private static PodsListAdminList gFinalResultListAdminListModel = null;

    private static Pods gResultModel = null;
    private static Pods gFinalResultModel = null;

    private static PodsAdmin gResultAdminModel = null;
    private static PodsAdmin gFinalResultAdminModel = null;

    private static CommonResourcesYaml gResultYamlModel = null;
    private static CommonResourcesYaml gFinalResultYamlModel = null;

    private static ResultStatus gResultStatusModel = null;
    private static ResultStatus gFinalResultStatusModel = null;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    PodsService podsService;

    @Before
    public void setUp() {
        gResultMap = new HashMap();

        gResultListModel = new PodsList();
        gFinalResultListModel = new PodsList();
        gFinalResultListModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gResultListAdminModel = new PodsListAdmin();
        gFinalResultListAdminModel = new PodsListAdmin();
        gFinalResultListAdminModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gResultListAdminListModel = new PodsListAdminList();
        gFinalResultListAdminListModel = new PodsListAdminList();

        gResultModel = new Pods();
        gFinalResultModel = new Pods();
        gFinalResultModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gResultAdminModel = new PodsAdmin();
        gFinalResultAdminModel = new PodsAdmin();
        gFinalResultAdminModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gResultYamlModel = new CommonResourcesYaml();
        gFinalResultYamlModel = new CommonResourcesYaml();
        gFinalResultYamlModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultYamlModel.setSourceTypeYaml(YAML_STRING);

        gResultStatusModel = new ResultStatus();
        gFinalResultStatusModel = new ResultStatus();
        gFinalResultStatusModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Pods 목록 조회(Get Pods list) Test
     */
    @Test
    public void getPodsList_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods", HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.resourceListProcessing(gResultListModel, OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.setResultModel(gResultListModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListModel);

        // when
        PodsList resultList = podsService.getPodsList(NAMESPACE, OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    /**
     * Pods 목록 조회(Get Pods list) Test
     * (Admin Portal)
     */
    @Test
    public void getPodsListAdmin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods", HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.resourceListProcessing(gResultListAdminModel, OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME, PodsListAdmin.class)).thenReturn(gResultListAdminModel);

        // for test case need?

        // when
        PodsListAdmin resultList = (PodsListAdmin) podsService.getPodsListAdmin(NAMESPACE, OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    /**
     * Pods 목록 조회(Get Pods selector) Test
     */
    @Test
    public void getPodListWithLabelSelector_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API,"/api/v1/namespaces/" + NAMESPACE + "/pods?labelSelector=" + SELECTOR, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.setCommonItemMetaDataBySelector(gResultListModel, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.setResultModel(gResultListModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListModel);

        // when
        PodsList resultList = podsService.getPodListWithLabelSelector(NAMESPACE, SELECTOR);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    /**
     * Pods 목록 조회(Get Pods selector) Test
     */
    @Test
    public void getPodListWithLabelSelectorAdmin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods?labelSelector=" + SELECTOR, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.setCommonItemMetaDataBySelector(gResultListAdminModel, PodsListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.setResultModel(gResultListAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListAdminModel);

        // when
        PodsListAdmin resultList = podsService.getPodListWithLabelSelectorAdmin(NAMESPACE, SELECTOR);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    /**
     * Pods 목록 조회(Get Pods node) Test
     */
    @Test
    public void getPodListByNode_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/?fieldSelector=spec.nodeName=" + NODE_NAME, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.setCommonItemMetaDataBySelector(gResultListModel, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.setResultModel(gResultListModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListModel);

        // when
        PodsList resultList = podsService.getPodListByNode(NAMESPACE, NODE_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    /**
     * Pods 목록 조회(Get Pods node) Test
     * (Admin portal)
     */
    @Test
    public void getPodListByNodeAdmin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/?fieldSelector=spec.nodeName=" + NODE_NAME, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.setCommonItemMetaDataBySelector(gResultListAdminModel, PodsListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.setResultModel(gResultListAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListAdminModel);

        // when
        PodsListAdmin resultList = podsService.getPodListByNodeAdmin(NAMESPACE, NODE_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

    /**
     * Pods 상세 조회(Get Pods detail) Test
     */
    @Test
    public void getPods_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsGetUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/"+ PODS_NAME, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, Pods.class)).thenReturn(gResultModel);
        when(commonService.setResultModel(gResultModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultModel);

        // when
        Pods result = podsService.getPods(NAMESPACE, PODS_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Pods 상세 조회(Get Pods detail) Test
     * (Admin Portal)
     */
    @Test
    public void getPodsAdmin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsGetUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/"+ PODS_NAME, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);

        // try catch

        when(commonService.setResultObject(gResultMap, PodsAdmin.class)).thenReturn(gResultAdminModel);
        when(commonService.setResultModel(gResultAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultAdminModel);

        // when
        PodsAdmin result = (PodsAdmin) podsService.getPodsAdmin(NAMESPACE, PODS_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Pods YAML 조회(Get Pods yaml) Test
     */
    @Test
    public void getPodsYaml_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsGetUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/"+ PODS_NAME, HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML)).thenReturn(YAML_STRING);
        when(commonService.setResultObject(gResultMap, CommonResourcesYaml.class)).thenReturn(gResultYamlModel);
        when(commonService.setResultModel(gResultYamlModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultYamlModel);

        // when
        CommonResourcesYaml result = (CommonResourcesYaml) podsService.getPodsYaml(NAMESPACE, PODS_NAME, gResultMap);

        // then
        assertEquals(YAML_STRING, result.getSourceTypeYaml());
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Pods Admin YAML 조회(Get Pods Admin yaml) Test
     */
    @Test
    public void getPodsAdminYaml_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsGetUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/"+ PODS_NAME, HttpMethod.GET, null, String.class, Constants.ACCEPT_TYPE_YAML)).thenReturn(YAML_STRING);

        /*
        if (CommonUtils.isResultStatusInstanceCheck(response)) {
            return response;
        }
        */

        when(commonService.setResultObject(gResultMap, CommonResourcesYaml.class)).thenReturn(gResultYamlModel);
        when(commonService.setResultModel(gResultYamlModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultYamlModel);

        // when
        CommonResourcesYaml result = (CommonResourcesYaml) podsService.getPodsAdminYaml(NAMESPACE, PODS_NAME, gResultMap);

        // then
        assertEquals(YAML_STRING, result.getSourceTypeYaml());
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Pods 생성(Create Pods) Test
     */
    @Test
    public void createPods_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsCreateUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods");
        when(restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods", HttpMethod.POST, YAML_STRING, Object.class, isAdmin)).thenReturn(gResultStatusModel);
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS)).thenReturn(gFinalResultStatusModel);

        // when
        ResultStatus result = (ResultStatus) podsService.createPods(NAMESPACE, YAML_STRING, isAdmin);

        // then
        assertEquals(gFinalResultStatusModel, result);
    }

    /**
     * Pods 삭제(Delete Pods) Admin Test
     */
    @Test
    public void deletePods_Admin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsDeleteUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/" + PODS_NAME, HttpMethod.DELETE, null, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS)).thenReturn(gFinalResultStatusModel);

        // when
        ResultStatus result = podsService.deletePods(NAMESPACE, PODS_NAME, isAdmin);

        // then
        assertEquals(gFinalResultStatusModel, result);
    }

    /**
     * Pods 삭제(Delete Pods) Not Admin Test
     */
    @Test
    public void deletePods_Not_Admin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsDeleteUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/" + PODS_NAME, HttpMethod.DELETE, null, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS)).thenReturn(gFinalResultStatusModel);

        // when
        ResultStatus result = podsService.deletePods(NAMESPACE, PODS_NAME, isNotAdmin);

        // then
        assertEquals(gFinalResultStatusModel, result);
    }

    /**
     * Pods 수정(Update Pods) Test
     */
    @Test
    public void updatePods_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsUpdateUrl()).thenReturn("/api/v1/namespaces/{namespace}/pods/{name}");
        when(restTemplateService.sendYaml(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/pods/" + PODS_NAME, HttpMethod.PUT, YAML_STRING, Object.class, isAdmin)).thenReturn(gResultStatusModel);
        when(commonService.setResultObject(gResultStatusModel, ResultStatus.class)).thenReturn(gResultStatusModel);
        when(commonService.setResultModelWithNextUrl(gResultStatusModel, Constants.RESULT_STATUS_SUCCESS, Constants.URI_WORKLOAD_PODS_DETAIL.replace("{podName:.+}", PODS_NAME))).thenReturn(gFinalResultStatusModel);

        // when
        ResultStatus result = (ResultStatus) podsService.updatePods(NAMESPACE, PODS_NAME, YAML_STRING, isAdmin);

        // then
        assertEquals(gFinalResultStatusModel, result);
    }

    /**
     * 전체 Namespaces 의 Pods Admin 목록 조회(Get Services Admin list in all namespaces) Test
     */
    @Test
    public void getPodsListAllNamespacesAdmin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListPodsListAllNamespacesUrl()).thenReturn("/api/v1/pods");
        when(commonService.generateFieldSelectorForExceptNamespace(Constants.RESOURCE_NAMESPACE)).thenReturn(FIELD_SELECTOR);
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/pods/" + FIELD_SELECTOR, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsListAdmin.class)).thenReturn(gResultListAdminModel);
        when(commonService.resourceListProcessing(gResultListAdminModel, OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME, PodsListAdmin.class)).thenReturn(gResultListAdminModel);

        // for

        when(commonService.setResultModel(gResultListAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListAdminModel);

        // when
        PodsListAdmin resultList =  (PodsListAdmin) podsService.getPodsListAllNamespacesAdmin(OFFSET, LIMIT, ORDER_BY, ORDER, SEARCH_NAME);

        // then
        assertEquals(gFinalResultListAdminModel, resultList);
    }
}
