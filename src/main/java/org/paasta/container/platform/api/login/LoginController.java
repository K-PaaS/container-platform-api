package org.paasta.container.platform.api.login;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersList;
import org.paasta.container.platform.api.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    private UsersService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public Object generateToken(@RequestBody AuthenticationRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUserId(), authRequest.getPassword()));
        } catch (Exception e) {

            ResultStatus resultStatus = new ResultStatus(Constants.RESULT_STATUS_FAIL, Constants.LOGIN_FAIL,
                    CommonStatusCode.UNAUTHORIZED.getCode(), e.getMessage());

            return resultStatus;
        }

        //Generate token
        UserDetails userdetails = userDetailsService.loadUserByUsername(authRequest.getUserId());
        String token = jwtUtil.generateToken(userdetails);

        // Extract the namespace list that contains the user
        UsersList userListByUserId = userService.getUsersDetails(authRequest.getUserId());
        List<Users> userItem = userListByUserId.getItems();

         List namespaceList = new ArrayList();

        for ( Users user : userItem) {
            namespaceList.add(user.getCpNamespace());
        };

        AuthenticationResponse authResponse = new AuthenticationResponse(Constants.RESULT_STATUS_SUCCESS, Constants.LOGIN_SUCCESS, CommonStatusCode.OK.getCode(),
                Constants.LOGIN_SUCCESS, Constants.URI_INTRO_OVERVIEW, userdetails.getUsername(), token, namespaceList ) ;

        return authResponse;
    }


}
