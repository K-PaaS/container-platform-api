package org.paasta.container.platform.api.login;

import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
	private final UsersService usersService;

	@Autowired
	public CustomUserDetailsService(UsersService usersService) {
		this.usersService = usersService;
	}

	@Autowired
	private HttpServletRequest request;

	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		List<SimpleGrantedAuthority> roles = null;
		String isAdmin = null ;
		isAdmin =  request.getParameter("isAdmin");

		if(isAdmin == null) { isAdmin = "false" ; }

		Users user = usersService.getUsersDetailsForLogin(userId, isAdmin);
     	if (user != null) {
			roles = Arrays.asList(new SimpleGrantedAuthority(user.getUserType()));

			return new User(user.getUserId(), user.getPassword(), roles);
		}
		throw new UsernameNotFoundException(Constants.NON_EXISTENT_ID);	}


}
