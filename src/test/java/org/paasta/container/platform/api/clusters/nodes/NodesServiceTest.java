package org.paasta.container.platform.api.clusters.nodes;

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
public class NodesServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    NodesService nodesService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getNodesList() {
    }

    @Test
    public void getNodesListAdmin() {
    }

    @Test
    public void getNodes() {
    }

    @Test
    public void getNodesAdmin() {
    }

    @Test
    public void getNodesYaml() {
    }
}