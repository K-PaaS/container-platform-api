package org.paasta.container.platform.api.workloads.deployments;

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
public class DeploymentsServiceTest {

    private static final String NAMESPACE = "test-namespace";
    private static final String DEPLOYMENT_NAME = "test-deployment-name";
    private static final String LABEL_SELECTOR = "test-label-selector";
    private static final String YAML_STRING = "test-yaml-string";

    private static HashMap gResultMap = null;

    private static DeploymentsList gResultListModel = null;
    private static DeploymentsList gFinalResultListModel = null;
    private static DeploymentsList gFinalResultListFailModel = null;

    private static Deployments gResultModel = null;
    private static Deployments gFinalResultModel = null;
    private static Deployments gFinalResultFailModel = null;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    DeploymentsService deploymentsService;

    @Before
    public void setUp() {

        // 리스트가져옴
        gResultMap = new HashMap();
        gResultListModel = new DeploymentsList();
        gFinalResultListModel = new DeploymentsList();
        gFinalResultListModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gFinalResultListFailModel = new DeploymentsList();
        gFinalResultListFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);

        // 하나만 가져옴
        gResultModel = new Deployments();
        gFinalResultModel = new Deployments();
        gFinalResultModel.setSourceTypeYaml(YAML_STRING);
        gFinalResultModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gFinalResultFailModel = new Deployments();
        gFinalResultFailModel.setResultCode(Constants.RESULT_STATUS_FAIL);

    }

    @Test
    public void getDeploymentList_Valid_ReturnModel() {

        //when
        when(propertyService.getCpMasterApiListDeploymentsListUrl()).thenReturn("/apis/apps/v1/namespaces/{namespace}/deployments");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/apis/apps/v1/namespaces/" + NAMESPACE +"/deployments", HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, DeploymentsList.class)).thenReturn(gResultListModel);
        when(commonService.setResultModel(gResultListModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultListModel);

        //call method
        DeploymentsList resultList = deploymentsService.getDeploymentsList(NAMESPACE, 0, 0, "creationTime", "desc", "");

        //compare result
        assertThat(resultList).isNotNull();
        assertEquals(Constants.RESULT_STATUS_SUCCESS, resultList.getResultCode());
    }

}
