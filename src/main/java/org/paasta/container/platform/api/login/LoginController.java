package org.paasta.container.platform.api.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.config.NoAuth;
import org.paasta.container.platform.api.login.support.loginMetaDataItem;
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
@Api(value = "LoginController v1")
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UsersService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 사용자 로그인(User login)
     *
     * @param authRequest the AuthenticationRequest
     * @return return is succeeded
     */
    @ApiOperation(value = "사용자 로그인(User login)", nickname = "generateToken")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authRequest", value = "로그인을 위한 사용자 정보", required = true, dataType = "object", paramType = "body"),
            @ApiImplicitParam(name = "isAdmin", value = "관리자 여부 (true/false)",  required = true, dataType = "string", paramType = "query")
    })
    @NoAuth
    @PostMapping
    @ResponseBody
    public Object generateToken(@RequestBody AuthenticationRequest authRequest,
                                @RequestParam(required = true, name = "isAdmin", defaultValue = "false") String isAdmin) {
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

        List<loginMetaDataItem> loginMetaData = new ArrayList<>();

        for ( Users user : userItem) {
           loginMetaData.add(new loginMetaDataItem(user.getCpNamespace(), user.getUserType()));
        };

        AuthenticationResponse authResponse = new AuthenticationResponse(Constants.RESULT_STATUS_SUCCESS, Constants.LOGIN_SUCCESS, CommonStatusCode.OK.getCode(),
                Constants.LOGIN_SUCCESS, Constants.URI_INTRO_OVERVIEW, userdetails.getUsername(), token, loginMetaData ) ;

        return authResponse;
    }


}
