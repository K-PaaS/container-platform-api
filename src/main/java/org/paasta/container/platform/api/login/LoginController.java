package org.paasta.container.platform.api.login;

import org.paasta.container.platform.api.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login Controller 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@RestController
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Object generateToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUserId(), authRequest.getPassword()));
        } catch (Exception e) {

            AuthenticationResponse authResponse = new AuthenticationResponse(Constants.RESULT_STATUS_FAIL, "Login Failed.", 401,
                    e.getMessage(), null, null, null);

            return authResponse;
        }

        UserDetails userdetails = userDetailsService.loadUserByUsername(authRequest.getUserId());
        String token = jwtUtil.generateToken(userdetails);

        AuthenticationResponse authResponse = new AuthenticationResponse(Constants.RESULT_STATUS_SUCCESS, "Login Successful.", 200,
                "Login Successful.", Constants.URI_INTRO_OVERVIEW, userdetails.getUsername(), token);
        return authResponse;
    }


}
