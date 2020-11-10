package org.paasta.container.platform.api.adminToken;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class AdminTokenServiceTest {
    @Mock
    RestTemplateService restTemplateService;
}