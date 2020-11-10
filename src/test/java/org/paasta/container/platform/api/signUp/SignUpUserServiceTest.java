package org.paasta.container.platform.api.signUp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.accessInfo.AccessTokenService;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.ResourceYamlService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.users.UsersService;
import org.paasta.container.platform.api.workloads.deployments.DeploymentsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class SignUpUserServiceTest {

    @Mock
    CommonService commonService;

    @Mock
    PropertyService propertyService;

    @Mock
    RestTemplateService restTemplateService;

    @Mock
    UsersService usersService;

    @Mock
    ResourceYamlService resourceYamlService;

    @InjectMocks
    SignUpUserService signUpUserService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void signUpUsers() {
    }

    @Test
    public void getUsersNameList() {
    }
}