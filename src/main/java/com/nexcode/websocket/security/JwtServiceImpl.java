package com.nexcode.websocket.security;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {

	private static final String SECRET_KEY = "cb1d3671e8dcdfd12fd67a5121b73a61c78c5a5c63e74157d0c79c1e3a05ccc9";

	@Override
	public String extractUsername(String jwt) {
		return extractClaim(jwt, Claims::getSubject);
	}

	public <T> T extractClaim(String jwt, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(jwt);
		return claimsResolver.apply(claims);
	}

	private Claims extractAllClaims(String jwt) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwt).getBody();
	}

	private Key getSignInKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}

	@Override
	public boolean isTokenValid(String jwt) {
		// final String username = extractUsername(jwt);
		return !isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		return extractExpiration(jwt).before(new Date());
	}

	private Date extractExpiration(String jwt) {
		return extractClaim(jwt, Claims::getExpiration);
	}

	@Override
	public Claims getClaims(String jwt) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(jwt).getBody();
	}

	@Override
	public String generateToken(Authentication authentication) {
		UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
		return generateToken(userPrincipal);
	}

	private String generateToken(UserPrincipal user) {
		final String authorities = user.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.joining(","));
		String userId = user.getId().toString();
		return Jwts.builder().setId(userId).claim("roles", authorities).setSubject(user.getUsername())
				.setIssuedAt(new Date()).setExpiration(new Date((new Date()).getTime() + 86400 * 1000))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();
	}

}
