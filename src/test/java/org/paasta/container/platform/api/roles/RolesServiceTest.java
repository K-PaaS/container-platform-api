package org.paasta.container.platform.api.roles;

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
public class RolesServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    RolesService rolesService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getRolesList() {
    }

    @Test
    public void getRoles() {
    }

    @Test
    public void getRolesYaml() {
    }

    @Test
    public void createRoles() {
    }

    @Test
    public void deleteRoles() {
    }

    @Test
    public void updateRoles() {
    }

    @Test
    public void getRolesListAdmin() {
    }

    @Test
    public void getRolesAdmin() {
    }

    @Test
    public void getRolesListAllNamespacesAdmin() {
    }
}