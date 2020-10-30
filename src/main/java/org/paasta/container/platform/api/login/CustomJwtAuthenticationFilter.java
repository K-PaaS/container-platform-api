package org.paasta.container.platform.api.login;

import io.jsonwebtoken.ExpiredJwtException;
import org.paasta.container.platform.api.common.MethodHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Custom Jwt Authentication Filter 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Component
public class CustomJwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtTokenUtil;

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandler.class);

	@Value("${server.auth.valid}")
	private String AuthTokenValid;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try{

			String jwtToken = extractJwtFromRequest(request);

			String agent = request.getHeader("User-Agent");
			String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
			if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
				clientIp = request.getHeader("REMOTE_ADDR");
			}
			if (null == clientIp || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) {
				clientIp = request.getRemoteAddr();
			}

			if (StringUtils.hasText(jwtToken) && jwtTokenUtil.validateToken(jwtToken)) {
				UserDetails userDetails = new User(jwtTokenUtil.getUsernameFromToken(jwtToken), "",
						jwtTokenUtil.getRolesFromToken(jwtToken));

				String tokenIp = jwtTokenUtil.getClientIpFromToken(jwtToken);
				LOGGER.error("agent: {} || clientIp: {} || tokenIp {}", agent, clientIp, tokenIp);

				if(AuthTokenValid.equals("Y")) {
					if (clientIp.equals(tokenIp) && agent.indexOf("Java") >= 0) {
						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					} else {
						LOGGER.info("The connection information is different.");
						LOGGER.warn("agent: {} || clientIp: {} || tokenIp {}", agent, clientIp, tokenIp);
					}
				}else{
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}

			} else {
				LOGGER.info("Cannot set the Security Context");
			}
		} catch (ExpiredJwtException ex) {
			request.setAttribute("exception", ex);

		} catch (BadCredentialsException ex) {
			request.setAttribute("exception", ex);

		}
		chain.doFilter(request, response);
	}

	private String extractJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7, bearerToken.length());
		}
		return null;
	}

}