package org.paasta.container.platform.api.login;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.paasta.container.platform.api.clusters.limitRanges.LimitRangesService;
import org.paasta.container.platform.api.common.CommonService;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.RestTemplateService;
import org.paasta.container.platform.api.users.UsersService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@TestPropertySource("classpath:application.yml")
public class CustomUserDetailsServiceTest {

    @Mock
    UsersService usersService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    AuthenticationManager authenticationManager;

    @InjectMocks
    CustomUserDetailsService userDetailsService;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void loadUserByUsername() {
    }

    @Test
    public void createAuthenticationResponse() {
    }

    @Test
    public void defaultNamespaceFilter() {
    }
}