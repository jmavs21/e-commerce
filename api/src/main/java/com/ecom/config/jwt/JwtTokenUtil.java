package com.ecom.config.jwt;

import java.security.Key;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.ecom.domain.users.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

/** Creates and validates JSON Web Tokens by using jsonwebtoken library. */
@Component
public class JwtTokenUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration.millis}")
  private String jwtExpirationMillis;

  public String getUsernameFromToken(String token) {
    return getAllClaimsFromToken(token).getSubject();
  }

  public String generateToken(User user) {
    return Jwts.builder()
        .setClaims(getclaims(user))
        .setSubject(user.getEmail())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(jwtExpirationMillis)))
        .signWith(getSigningKey())
        .compact();
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    return (getUsernameFromToken(token).equals(userDetails.getUsername())
        && !isTokenExpired(token));
  }

  private Claims getAllClaimsFromToken(String token) {
    return Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody();
  }

  private Map<String, Object> getclaims(User user) {
    return Map.of("id", user.getId(), "name", user.getName(), "email", user.getEmail());
  }

  private Boolean isTokenExpired(String token) {
    return getAllClaimsFromToken(token).getExpiration().before(new Date());
  }

  private Key getSigningKey() {
    return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
  }
}
