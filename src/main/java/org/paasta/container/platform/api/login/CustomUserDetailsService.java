package org.paasta.container.platform.api.login;

import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Custom UserDetails Service 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {


	@Autowired
	private final UsersService usersService;

	@Autowired
	public CustomUserDetailsService(UsersService usersService) {
		this.usersService = usersService;
	}


	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = null;

         Users user = usersService.getUsersDetailsForLogin(userId);
     	if (user != null) {
			roles = Arrays.asList(new SimpleGrantedAuthority(user.getUserType()));

			return new User(user.getUserId(), user.getPassword(), roles);
		}
		throw new UsernameNotFoundException("This ID does not exist.");	}


}
