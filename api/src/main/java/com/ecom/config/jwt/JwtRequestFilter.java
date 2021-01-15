package com.ecom.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ecom.config.web.MyUserDetailsService;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;

/**
 * This filter gets executed for any incoming request. The filter checks if the request has a JWT
 * token in its headers and if the token is valid, then authenticates the user.
 */
@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

  public static final String X_AUTH_TOKEN = "x-auth-token";

  private MyUserDetailsService myUserDetailsService;
  private JwtTokenUtil jwtTokenUtil;

  public JwtRequestFilter(MyUserDetailsService myUserDetailsService, JwtTokenUtil jwtTokenUtil) {
    this.myUserDetailsService = myUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String jwtToken = request.getHeader(X_AUTH_TOKEN);
    String username = getValidUsernameFromToken(jwtToken);
    if (jwtToken != null && username != null) authenticateUser(request, jwtToken, username);
    filterChain.doFilter(request, response);
  }

  private String getValidUsernameFromToken(String jwtToken) {
    if (jwtToken != null && jwtToken.contains(".")) {
      try {
        return jwtTokenUtil.getUsernameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        log.error("Unable to get JWT Token.");
      } catch (ExpiredJwtException e) {
        log.error("JWT Token has expired.");
      }
    }
    return null;
  }

  private void authenticateUser(HttpServletRequest request, String jwtToken, String username) {
    if (SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);
      if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
        UsernamePasswordAuthenticationToken usernameAuthToken =
            new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        usernameAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(usernameAuthToken);
      }
    }
  }
}
