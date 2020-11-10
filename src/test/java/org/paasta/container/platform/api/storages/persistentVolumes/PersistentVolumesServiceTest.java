package org.paasta.container.platform.api.storages.persistentVolumes;

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
public class PersistentVolumesServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    PersistentVolumesService persistentVolumesService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getPersistentVolumesListAdmin() {
    }

    @Test
    public void getPersistentVolumesYaml() {
    }

    @Test
    public void createPersistentVolumes() {
    }

    @Test
    public void deletePersistentVolumes() {
    }

    @Test
    public void updatePersistentVolumes() {
    }

    @Test
    public void getPersistentVolumesAdmin() {
    }

    @Test
    public void getPersistentVolumesListAllNamespacesAdmin() {
    }
}