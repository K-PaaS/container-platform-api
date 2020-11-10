package org.paasta.container.platform.api.clusters.resourceQuotas;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsService;

import static org.junit.Assert.*;

public class ResourceQuotasServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    ResourceQuotasService resourceQuotasService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getResourceQuotasList() {
    }

    @Test
    public void getResourceQuotasListAdmin() {
    }

    @Test
    public void getResourceQuotas() {
    }

    @Test
    public void getResourceQuotasAdmin() {
    }

    @Test
    public void getResourceQuotasYaml() {
    }

    @Test
    public void createResourceQuotas() {
    }

    @Test
    public void deleteResourceQuotas() {
    }

    @Test
    public void updateResourceQuotas() {
    }

    @Test
    public void getRqDefaultList() {
    }

    @Test
    public void getResourceQuotasListAllNamespacesAdmin() {
    }
}