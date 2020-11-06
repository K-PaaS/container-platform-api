package org.paasta.container.platform.api.login;

import org.paasta.container.platform.api.common.MessageConstant;
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
            throw new InternalAuthenticationServiceException(MessageConstant.ID_PASSWORD_REQUIRED);
        }

        String username = authentication.getPrincipal().toString(); //USER ID
        String password = authentication.getCredentials().toString(); //USER PASSWORD


        if( username == null || username.length() < 1) {
            throw new AuthenticationCredentialsNotFoundException(MessageConstant.ID_REQUIRED);
        }

        if( password == null || password.length() < 1) {
            throw new AuthenticationCredentialsNotFoundException(MessageConstant.PASSWORD_REQUIRED);
        }

        UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException(MessageConstant.NON_EXISTENT_ID);
        }
        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException(MessageConstant.UNAVAILABLE_ID);
        }
        if (!loadedUser.isEnabled()) {
            throw new DisabledException(MessageConstant.UNAVAILABLE_ID);
        }
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException(MessageConstant.UNAVAILABLE_ID);
        }
        if (!passwordEncoder.matches(password, loadedUser.getPassword())) {
            throw new BadCredentialsException(MessageConstant.INVALID_PASSWORD);
        }
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException(MessageConstant.UNAVAILABLE_ID);
        }
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    } }

