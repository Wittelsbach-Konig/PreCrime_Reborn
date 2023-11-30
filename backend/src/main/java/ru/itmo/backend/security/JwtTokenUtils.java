package ru.itmo.backend.security;

import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtils {
	private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
	
	public String generateToken(Authentication authentication) {
		Map<String, Object> claims = new HashMap<>();
		List<String> rolesList = authentication.getAuthorities().stream()
						.map(GrantedAuthority::getAuthority)
						.toList();
		claims.put("roles", rolesList);
		String username = authentication.getName();
		Date currentDate = new Date();
		Date expireDate = new Date(currentDate.getTime() + SecurityLiterals.JWT_EXPIRATION);

        return Jwts.builder()
				.setClaims(claims)
				.setSubject(username)
				.setIssuedAt( new Date())
				.setExpiration(expireDate)
				.signWith(key,SignatureAlgorithm.HS512)
				.compact();
	}
	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(key)
				.build()
				.parseClaimsJws(token)
				.getBody();
	}
	public String getUsernameFromJWT(String token) {
		return getAllClaimsFromToken(token).getSubject();
	}
	public List<String> getRolesFromJWT(String token) {
		return getAllClaimsFromToken(token).get("roles", List.class);
	}
//	public String getUsernameFromJWT(String token){
//		Claims claims = Jwts.parserBuilder()
//				.setSigningKey(key)
//				.build()
//				.parseClaimsJws(token)
//				.getBody();
//		return claims.getSubject();
//	}
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(key)
			.build()
			.parseClaimsJws(token);
			return true;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}

}
