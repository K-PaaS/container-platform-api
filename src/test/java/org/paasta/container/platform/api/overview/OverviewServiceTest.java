package org.paasta.container.platform.api.overview;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.clusters.namespaces.NamespacesService;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.users.UsersService;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsService;
import org.paasta.container.platform.api.workloads.pods.PodsService;
import org.paasta.container.platform.api.workloads.replicaSets.ReplicaSetsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class OverviewServiceTest {
    @Mock
    NamespacesService namespacesService;

    @Mock
    DeploymentsService deploymentsService;

    @Mock
    PodsService podsService;

    @Mock
    ReplicaSetsService replicaSetsService;

    @Mock
    UsersService usersService;

    @Mock
    CommonService commonService;

    @InjectMocks
    OverviewService overviewService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getOverviewAll() {
    }

    @Test
    public void getOverview() {
    }
}