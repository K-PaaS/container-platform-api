package org.paasta.container.platform.api.login;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.MessageConstant;
import org.paasta.container.platform.api.common.PropertyService;
import org.paasta.container.platform.api.common.model.CommonStatusCode;
import org.paasta.container.platform.api.common.model.ResultStatus;
import org.paasta.container.platform.api.login.support.loginMetaDataItem;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersList;
import org.paasta.container.platform.api.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Custom User Details Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private final PropertyService propertyService;

    private final UsersService usersService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    public CustomUserDetailsService(PropertyService propertyService, UsersService usersService) {
        this.propertyService = propertyService;
        this.usersService = usersService;
    }

    @Autowired
    private HttpServletRequest request;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        List<SimpleGrantedAuthority> roles = null;
        String isAdmin = null;
        isAdmin = request.getParameter("isAdmin");

        if (isAdmin == null) {
            isAdmin = "false";
        }

        Users user = usersService.getUsersDetailsForLogin(userId, isAdmin);
        if (user != null) {
            roles = Arrays.asList(new SimpleGrantedAuthority(user.getUserType()));
            return new User(user.getUserId(), user.getPassword(), roles);
        }
        throw new UsernameNotFoundException(MessageConstant.NON_EXISTENT_ID);
    }


    public Object createAuthenticationResponse(AuthenticationRequest authRequest) {

        AuthenticationResponse authResponse = new AuthenticationResponse();

        UserDetails userdetails = loadUserByUsername(authRequest.getUserId());

        UsersList userListByUserId = usersService.getUsersDetails(authRequest.getUserId());
        List<Users> userItem = userListByUserId.getItems();

        //Generate token
        String token = jwtUtil.generateToken(userdetails, authRequest, userListByUserId);

        //user_auth get
        String user_auth = userdetails.getAuthorities().toArray()[0].toString();


        // CLUSTER_ADMIN
        if (user_auth.equals(Constants.AUTH_CLUSTER_ADMIN)) {

            Users user = usersService.getUsersDetailsForLogin(userdetails.getUsername(), "true");

            authResponse = new AuthenticationResponse(Constants.RESULT_STATUS_SUCCESS, MessageConstant.LOGIN_SUCCESS, CommonStatusCode.OK.getCode(),
                    MessageConstant.LOGIN_SUCCESS, Constants.URI_INTRO_OVERVIEW, userdetails.getUsername(), token, null,  user.getClusterName());
        }
        // NAMESPACE_ADMIN, USER
        else {

            Users user = usersService.getUsersDetailsForLogin(userdetails.getUsername(), "false");

            //generate loginMetadata & filter default namespace
            List<loginMetaDataItem> loginMetaData = defaultNamespaceFilter(userItem);

            if (loginMetaData.size() == 0) {
                //in-active user
                return new ResultStatus(Constants.RESULT_STATUS_FAIL, MessageConstant.LOGIN_FAIL, CommonStatusCode.FORBIDDEN.getCode(), MessageConstant.INACTIVE_USER_ACCESS);
            }

            authResponse = new AuthenticationResponse(Constants.RESULT_STATUS_SUCCESS, MessageConstant.LOGIN_SUCCESS, CommonStatusCode.OK.getCode(),
                    MessageConstant.LOGIN_SUCCESS, Constants.URI_INTRO_OVERVIEW, userdetails.getUsername(), token, loginMetaData, user.getClusterName());

        }


        return authResponse;
    }


    public List<loginMetaDataItem> defaultNamespaceFilter(List<Users> userItem) {

        List<loginMetaDataItem> loginMetaData = new ArrayList<>();

        for (Users user : userItem) {

            if (!user.getUserType().equals(Constants.AUTH_CLUSTER_ADMIN)) {

                if (!user.getCpNamespace().equals(propertyService.getDefaultNamespace())) {
                    loginMetaData.add(new loginMetaDataItem(user.getCpNamespace(), user.getUserType()));
                }

            }
        }

        return loginMetaData;
    }

}
