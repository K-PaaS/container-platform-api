package org.paasta.container.platform.api.workloads.pods;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class PodsServiceTest {

    private static final String NAMESPACE = "test-namespace";
    private static final String PODS_NAME = "test-pods-name";
    private static final String SELECTOR = "test-selector";
    private static final String NODE_NAME = "test-node";
    private static final String YAML_STRING = "test-yaml-string";

    private static HashMap gResultMap = null;

    private static PodsList gResultListModel = null;
    private static PodsList gFinalResultListModel = null;
    private static PodsList gFinalResultListFailModel = null;

    private static Pods gResultModel = null;
    private static Pods gFinalResultModel = null;
    private static Pods gFinalResultFailModel = null;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    PodsService podsService;

    /**
     * Sets up.
     */
    @Before
    public void setUp() {
        gResultMap = new HashMap();
        gResultListModel = new PodsList();
        gFinalResultListModel = new PodsList();
        gFinalResultListModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gFinalResultListFailModel = new PodsList();
        gFinalResultListFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);

        gResultModel = new Pods();
        gFinalResultModel = new Pods();
        gFinalResultModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gFinalResultFailModel = new Pods();
        gFinalResultFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);

    }

    /**
     * Pods 목록을 조회할 때에 대한 테스트 케이스.
     */
    @Test
    public void getPodsList_Valid_ReturnModel() {
        when(propertyService.getCpMasterApiListPodsListUrl()).thenReturn("/apis/apps/v1/pods");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/apis/apps/v1/pods", HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, PodsList.class)).thenReturn(gResultListModel);
        when(commonService.setResultModel(gResultListModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListModel);

        PodsList resultList = podsService.getPodsList(NAMESPACE, 0, 0, "creationTime", "desc", "");

        assertThat(resultList).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

}
