package org.paasta.container.platform.api.clusters.namespaces;

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
public class NamespacesServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    NamespacesService namespacesService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getNamespaces() {
    }

    @Test
    public void getNamespacesAdmin() {
    }

    @Test
    public void getNamespacesList() {
    }

    @Test
    public void getNamespacesListAdmin() {
    }

    @Test
    public void getNamespacesYaml() {
    }

    @Test
    public void deleteNamespaces() {
    }

    @Test
    public void createInitNamespaces() {
    }

    @Test
    public void modifyInitNamespaces() {
    }

    @Test
    public void getNamespacesListForSelectbox() {
    }
}