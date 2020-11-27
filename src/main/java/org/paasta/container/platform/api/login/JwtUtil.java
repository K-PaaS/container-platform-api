package org.paasta.container.platform.api.login;

import io.jsonwebtoken.*;
import org.paasta.container.platform.api.common.Constants;
import org.paasta.container.platform.api.common.MessageConstant;
import org.paasta.container.platform.api.users.Users;
import org.paasta.container.platform.api.users.UsersList;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * JwtUtil 클래스
 *
 * @author kjhoon
 * @version 1.0
 * @since 2020.09.28
 */
@Service
public class JwtUtil {

    private String secret;
    public static int jwtExpirationInMs;

    @Value("${jwt.secret}")
    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Value("${jwt.expirationDateInMs}")
    public void setJwtExpirationInMs(int jwtExpirationInMs) {
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public String generateToken(UserDetails userDetails, AuthenticationRequest authRequest, UsersList userListByUserId) {
        Map<String, Object> claims = new HashMap<>();
        String url = null;
        Collection<? extends GrantedAuthority> roles = userDetails.getAuthorities();

        if (roles.contains(new SimpleGrantedAuthority(Constants.AUTH_CLUSTER_ADMIN))) {
            claims.put("isClusterAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority(Constants.AUTH_NAMESPACE_ADMIN))) {
            claims.put("isNamespaceAdmin", true);
        }
        if (roles.contains(new SimpleGrantedAuthority(Constants.AUTH_USER))) {
            claims.put("isUser", true);
        }

        claims.put("IP", authRequest.getClientIp());
        claims.put("Browser", authRequest.getBrowser());
        for (Users users : userListByUserId.getItems())
            claims.put("url", users.getClusterApiUrl());

        return doGenerateToken(claims, userDetails.getUsername());
    }

    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(SignatureAlgorithm.HS512, secret).compact();

    }

    public boolean validateToken(String authToken) {
        try {

            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException(MessageConstant.LOGIN_INVALID_CREDENTIALS, ex);
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtException(null, null, MessageConstant.LOGIN_TOKEN_EXPIRED, ex);
        }
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

    public List<SimpleGrantedAuthority> getRolesFromToken(String authToken) {
        List<SimpleGrantedAuthority> roles = null;
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
        Boolean isClusterAdmin = claims.get("isClusterAdmin", Boolean.class);
        Boolean isNamespaceAdmin = claims.get("isNamespaceAdmin", Boolean.class);
        Boolean isUser = claims.get("isUser", Boolean.class);

        if (isClusterAdmin != null && isClusterAdmin == true) {
            roles = Arrays.asList(new SimpleGrantedAuthority(Constants.AUTH_CLUSTER_ADMIN));
        }

        if (isNamespaceAdmin != null && isNamespaceAdmin == true) {
            roles = Arrays.asList(new SimpleGrantedAuthority(Constants.AUTH_NAMESPACE_ADMIN));
        }

        if (isUser != null && isUser == true) {
            roles = Arrays.asList(new SimpleGrantedAuthority(Constants.AUTH_USER));
        }
        return roles;
    }

    public String getClientIpFromToken(String authToken) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(authToken).getBody();
        String clientIp = String.valueOf(claims.get("IP"));

        return clientIp;
    }

    public String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }


}
