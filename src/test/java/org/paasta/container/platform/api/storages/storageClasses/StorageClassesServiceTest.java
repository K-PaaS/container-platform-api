package org.paasta.container.platform.api.storages.storageClasses;

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
public class StorageClassesServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    StorageClassesService storageClassesService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getStorageClassesListAdmin() {
    }

    @Test
    public void getStorageClassesAdmin() {
    }

    @Test
    public void getStorageClassesYaml() {
    }

    @Test
    public void createStorageClasses() {
    }

    @Test
    public void deleteStorageClasses() {
    }

    @Test
    public void updateStorageClasses() {
    }
}