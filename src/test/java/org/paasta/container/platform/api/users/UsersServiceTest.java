package org.paasta.container.platform.api.users;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.clusters.clusters.ClustersService;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.ResourceYamlService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class UsersServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    PropertyService propertyService;

    @Mock
    CommonService commonService;

    @Mock
    ResourceYamlService resourceYamlService;

    @Mock
    AccessTokenService accessTokenService;

    @Mock
    ClustersService clustersService;

    @InjectMocks
    UsersService usersService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getUsersAll() {
    }

    @Test
    public void getUsersAllByCluster() {
    }

    @Test
    public void getUsersListByNamespaceAdmin() {
    }

    @Test
    public void getUsersListByNamespace() {
    }

    @Test
    public void getUsers() {
    }

    @Test
    public void getUsersNameListByNamespace() {
    }

    @Test
    public void getUsersDetailsForLogin() {
    }

    @Test
    public void getUsersByNamespaceAndNsAdmin() {
    }

    @Test
    public void getUsersDetails() {
    }

    @Test
    public void testGetUsers() {
    }

    @Test
    public void createUsers() {
    }

    @Test
    public void updateUsers() {
    }

    @Test
    public void registerUsers() {
    }

    @Test
    public void modifyUsersAdmin() {
    }

    @Test
    public void deleteUsers() {
    }

    @Test
    public void modifyUsers() {
    }

    @Test
    public void deleteUsersByAllNamespaces() {
    }

    @Test
    public void modifyUsersConfig() {
    }

    @Test
    public void getUsersListInNamespaceAdmin() {
    }

    @Test
    public void commonSaveClusterInfo() {
    }

    @Test
    public void getUsersNameListByNamespaceAdmin() {
    }

    @Test
    public void getRolesListAllNamespacesAdminByUserId() {
    }
}