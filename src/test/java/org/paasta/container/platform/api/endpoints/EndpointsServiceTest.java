package org.paasta.container.platform.api.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.endpoints.support.EndpointAddress;
import org.paasta.container.platform.api.endpoints.support.EndpointSubset;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class EndpointsServiceTest {
    private static final String NAMESPACE = "cp-namespace";
    private static final String ENDPOINTS_NAME = "cp-endpoints";

    private static HashMap gResultMap = null;

    private static Endpoints gResultModel = null;
    private static Endpoints gFinalResultModel = null;

    private static EndpointsAdmin gResultAdminModel = null;
    private static EndpointsAdmin gFinalResultAdminModel = null;

    private static EndpointSubset gResultSubsetModel = null;
    private static EndpointSubset gFinalResultSubsetModel = null;

    private static List<EndpointAddress> gResultListEndpointAddressModel = null;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @Mock
    EndpointsService endpointsServiceMock;

    @InjectMocks
    EndpointsService endpointsService;

    @Before
    public void setUp() {
        gResultMap = new HashMap();

        gResultModel = new Endpoints();
        gFinalResultModel = new Endpoints();
        gFinalResultModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);

        gResultAdminModel = new EndpointsAdmin();
        gFinalResultAdminModel = new EndpointsAdmin();
        gFinalResultAdminModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
    }

    /**
     * Endpoints 상세 조회(Get Endpoints list) Test
     * (User Portal)
     */
    @Test
    public void getEndpoints_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListEndpointsGetUrl()).thenReturn("/api/v1/namespaces/{namespace}/endpoints/{name}");
        when(restTemplateService.send(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/endpoints/" + ENDPOINTS_NAME, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, Endpoints.class)).thenReturn(gResultModel);
        when(commonService.setResultModel(gResultModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultModel);

        // when
        Endpoints result = endpointsService.getEndpoints(NAMESPACE, ENDPOINTS_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Endpoints 상세 조회(Get Endpoints detail) Test
     * (Admin Portal)
     */
    @Test
    public void getEndpointsAdmin_Valid_ReturnModel() {
        // given
        when(propertyService.getCpMasterApiListEndpointsGetUrl()).thenReturn("/api/v1/namespaces/{namespace}/endpoints/{name}");
        when(restTemplateService.sendAdmin(Constants.TARGET_CP_MASTER_API, "/api/v1/namespaces/" + NAMESPACE + "/endpoints/" + ENDPOINTS_NAME, HttpMethod.GET, null, Map.class)).thenReturn(gResultMap);
        when(commonService.setResultObject(gResultMap, EndpointsAdmin.class)).thenReturn(gResultAdminModel);
        when(endpointsService.endpointsAdminProcessing(gResultAdminModel));
        when(endpointsServiceMock.endpointsAdminProcessing(gResultAdminModel)).thenReturn(gResultAdminModel);
        when(commonService.setResultModel(gResultAdminModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultAdminModel);

        // when
        EndpointsAdmin result = (EndpointsAdmin) endpointsService.getEndpointsAdmin(NAMESPACE, ENDPOINTS_NAME);

        // then
        assertEquals(Constants.RESULT_STATUS_SUCCESS, result.getResultCode());
    }

    /**
     * Node 명에 따른 Node "Ready" 상태 값 조회 (Get Node "Ready" Status Value by Node Name) Test
     */
    @Test
    public void endpointsAdminProcessing_Valid_ReturnModel() {

    }
}