package org.paasta.container.platform.api.workloads.replicaSets;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.CommonResourcesYaml;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.workloads.pods.Pods;
import org.paasta.container.platform.api.workloads.pods.PodsAdmin;
import org.paasta.container.platform.api.workloads.pods.PodsList;
import org.paasta.container.platform.api.workloads.pods.PodsListAdmin;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class ReplicaSetsServiceTest {
    private static final String CLUSTER = "cp-cluster";
    private static final String NAMESPACE = "cp-namespace";
    private static final String SERVICE_NAME = "cp-service-name";
    private static final String YAML_STRING = "test-yaml-string";



    private static HashMap gResultMap = null;

    private static PodsList gResultListModel = null;
    private static PodsList gFinalResultListModel = null;

    private static PodsListAdmin gResultListAdminModel = null;
    private static PodsListAdmin gFinalResultListAdminModel = null;

    private static Pods gResultModel = null;
    private static Pods gFinalResultModel = null;

    private static PodsAdmin gResultAdminModel = null;
    private static PodsAdmin gFinalResultAdminModel = null;

    private static CommonResourcesYaml gResultYamlModel = null;
    private static CommonResourcesYaml gFinalResultYamlModel = null;

    private static ResultStatus gResultStatusModel = null;
    private static ResultStatus gResultFailModel = null;
    private static ResultStatus gFinalResultStatusModel = null;

    @Mock
    private RestTemplateService restTemplateService;

    @Mock
    private CommonService commonService;

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private ReplicaSetsService replicaSetsService;

    @Before
    public void setUp() {
        gResultMap = new HashMap();

    }

    @After
    public void tearDown() {
    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets list) Test
     */
    @Test
    public void getReplicaSetsList_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 상세 조회(Get ReplicaSets detail) Test
     */
    @Test
    public void getReplicaSets_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 상세 조회(Get ReplicaSets detail) Test
     * (Admin Portal)
     */
    @Test
    public void getReplicaSetsAdmin_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets YAML 조회(Get ReplicaSets yaml) Test
     */
    @Test
    public void getReplicaSetsYaml_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets Admin YAML 조회(Get ReplicaSets Admin yaml) Test
     */
    @Test
    public void getReplicaSetsAdminYaml_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets Selector) Test
     */
    @Test
    public void getReplicaSetsListLabelSelector_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets Selector) Test
     */
    @Test
    public void getReplicaSetsListLabelSelectorAdmin_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 목록 조회(Get ReplicaSets list) Test
     */
    @Test
    public void getReplicaSetsListAdmin_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 생성(Create ReplicaSets) Test
     */
    @Test
    public void createReplicaSets_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 삭제(Delete ReplicaSets) Test
     */
    @Test
    public void deleteReplicaSets_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * ReplicaSets 수정(Update ReplicaSets) Test
     */
    @Test
    public void updateReplicaSets_Valid_ReturnModel() {
        // given

        // when

        // then

    }

    /**
     * 전체 Namespaces 의 ReplicaSets Admin 목록 조회(Get ReplicaSets Admin list in all namespaces) Test
     */
    @Test
    public void getReplicaSetsListAllNamespacesAdmin_Valid_ReturnModel() {
        // given

        // when

        // then

    }
}
