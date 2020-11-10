package org.paasta.container.platform.api.customServices;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class CustomServicesServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    CustomServicesService customServicesService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getCustomServicesList() {
    }

    @Test
    public void getCustomServices() {
    }

    @Test
    public void getCustomServicesYaml() {
    }

    @Test
    public void createServices() {
    }

    @Test
    public void deleteServices() {
    }

    @Test
    public void updateServices() {
    }

    @Test
    public void getCustomServicesListAdmin() {
    }

    @Test
    public void getCustomServicesAdmin() {
    }

    @Test
    public void getCustomServicesListAllNamespacesAdmin() {
    }
}