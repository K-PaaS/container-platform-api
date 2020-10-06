package org.paasta.container.platform.api.login;

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
            throw new InternalAuthenticationServiceException("Please enter your user ID and Password.");
        }

        String username = authentication.getPrincipal().toString(); //USER ID
        String password = authentication.getCredentials().toString(); //USER PASSWORD


        if( username == null || username.length() < 1) {
            throw new AuthenticationCredentialsNotFoundException("Please enter your ID");
        }

        if( password == null || password.length() < 1) {
            throw new AuthenticationCredentialsNotFoundException("Please enter your Password");
        }

        UserDetails loadedUser = customUserDetailsService.loadUserByUsername(username);

        if (loadedUser == null) {
            throw new InternalAuthenticationServiceException("This ID does not exist.");
        }
        if (!loadedUser.isAccountNonLocked()) {
            throw new LockedException("This ID is locked");
        }
        if (!loadedUser.isEnabled()) {
            throw new DisabledException("This ID is disabled");
        }
        if (!loadedUser.isAccountNonExpired()) {
            throw new AccountExpiredException("This ID has expired");
        }
        if (!passwordEncoder.matches(password, loadedUser.getPassword())) {
            throw new BadCredentialsException("The password is not valid.");
        }
        if (!loadedUser.isCredentialsNonExpired()) {
            throw new CredentialsExpiredException("This ID has expired");
        }
        UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
        result.setDetails(authentication.getDetails());
        return result;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    } }

