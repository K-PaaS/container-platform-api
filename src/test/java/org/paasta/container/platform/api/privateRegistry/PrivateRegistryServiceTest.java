package org.paasta.container.platform.api.privateRegistry;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
class PrivateRegistryServiceTest {

    private static final String IMAGE_NAME = "registry";
    private static PrivateRegistry gFinalResultModel = null;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @InjectMocks
    PrivateRegistryService privateRegistryService;

    @Before
    public void setUp(){
        gFinalResultModel = new PrivateRegistry();
        gFinalResultModel.setResultCode(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultModel.setResultMessage(Constants.RESULT_STATUS_SUCCESS);
        gFinalResultModel.setHttpStatusCode(CommonStatusCode.OK.getCode());
        gFinalResultModel.setDetailMessage(CommonStatusCode.OK.getMsg());


    }
    @Test
    void getPrivateRegistry() {

        when(restTemplateService.sendAdmin(Constants.TARGET_COMMON_API, Constants.URI_COMMON_API_PRIVATE_REGISTRY.replace("{imageName:.+}", IMAGE_NAME), HttpMethod.GET, null, PrivateRegistry.class))
                .thenReturn(gFinalResultModel);
        when(commonService.setResultObject(gFinalResultModel, PrivateRegistry.class)).thenReturn(gFinalResultModel);
        when(commonService.setResultModel(gFinalResultModel, Constants.RESULT_STATUS_SUCCESS)).thenReturn(gFinalResultModel);
        PrivateRegistry privateRegistry = (PrivateRegistry) privateRegistryService.getPrivateRegistry(IMAGE_NAME);

        assertEquals(Constants.RESULT_STATUS_SUCCESS, privateRegistry.getResultCode());


    }
}