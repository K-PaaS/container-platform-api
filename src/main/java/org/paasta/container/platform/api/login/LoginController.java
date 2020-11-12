package org.paasta.container.platform.api.login;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.MessageConstant;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.config.NoAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

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


    /**
     * 사용자 로그인(User login)
     *
     * @param authRequest the AuthenticationRequest
     * @param isAdmin the isAdmin
     * @return return is succeeded
     */
    @ApiOperation(value = "사용자 로그인(User login)", nickname = "userLogin")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authRequest", value = "로그인을 위한 사용자 정보", required = true, dataType = "object", paramType = "body"),
            @ApiImplicitParam(name = "isAdmin", value = "관리자 여부 (true/false)", required = true, dataType = "string", paramType = "query")
    })
    @NoAuth
    @PostMapping
    @ResponseBody
    public Object userLogin(@RequestBody AuthenticationRequest authRequest,
                            @RequestParam(required = true, name = "isAdmin", defaultValue = "false") String isAdmin) {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequest.getUserId(), authRequest.getPassword()));
        } catch (Exception e) {

            ResultStatus resultStatus = new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.LOGIN_FAIL,
                    CommonStatusCode.UNAUTHORIZED.getCode(), e.getMessage());

            return resultStatus;
        }

        return userDetailsService.createAuthenticationResponse(authRequest);
    }
}
