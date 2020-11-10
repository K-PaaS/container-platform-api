package org.paasta.container.platform.api.storages.persistentVolumeClaims;

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
public class PersistentVolumeClaimsServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    PersistentVolumeClaimsService persistentVolumeClaimsService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getPersistentVolumeClaimsList() {
    }

    @Test
    public void getPersistentVolumeClaimsListAdmin() {
    }

    @Test
    public void getPersistentVolumeClaims() {
    }

    @Test
    public void getPersistentVolumeClaimsAdmin() {
    }

    @Test
    public void getPersistentVolumeClaimsYaml() {
    }

    @Test
    public void createPersistentVolumeClaims() {
    }

    @Test
    public void deletePersistentVolumeClaims() {
    }

    @Test
    public void updatePersistentVolumeClaims() {
    }

    @Test
    public void getPersistentVolumeClaimsListAllNamespacesAdmin() {
    }
}