package org.paasta.container.platform.api.events;

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
public class EventsServiceTest {

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @InjectMocks
    EventsService eventsService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getEventsList() {
    }

    @Test
    public void getEventsListAdmin() {
    }

    @Test
    public void getNamespaceEventsList() {
    }

    @Test
    public void getNamespaceEventsListAdmin() {
    }

    @Test
    public void generateFieldSelector() {
    }

    @Test
    public void generateCpMasterApiListEventsList() {
    }

    @Test
    public void generateLimitParam() {
    }
}