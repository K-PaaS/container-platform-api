package org.paasta.container.platform.api.login;

import org.apache.tomcat.util.bcel.Const;
import org.paasta.container.platform.api.common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Custom Authentication Provider 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (authentication == null) {
            throw new InternalAuthenticationServiceException(Constants.ID_PASSWORD_REQUIRED);
        }

        String username = authentication.getPrincipal().toString(); //USER ID
        String password = authentication.getCredentials().toString(); //USER PASSWORD


        if( username == null || username.length() < 1) {
            throw new AuthenticationCredentialsNotFoundException(Constants.ID_REQUIRED);
        }

        if( password == null || password.length() < 1) {
            throw new AuthenticationCredentialsNotFoundException(Constants.PASSWORD_REQUIRED);
        }

        UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(Constants.NON_EXISTENT_ID);
        }
        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException(Constants.UNAVAILABLE_ID);
        }
        if (!loadedUser.isEnabled()) {
            throw new DisabledException(Constants.UNAVAILABLE_ID);
        }
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException(Constants.UNAVAILABLE_ID);
        }
        if (!passwordEncoder.matches(password, loadedUser.getPassword())) {
            throw new BadCredentialsException(Constants.INVALID_PASSWORD);
        }
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(Constants.UNAVAILABLE_ID);
        }
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    } }

