package com.nexcode.websocket.service;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.nexcode.websocket.exception.AppException;
import com.nexcode.websocket.exception.ForbiddenException;
import com.nexcode.websocket.model.request.LoginRequest;
import com.nexcode.websocket.model.response.AuthenticationResponse;
import com.nexcode.websocket.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

	private final AuthenticationManager authenticationManager;

	private final JwtService jwtService;

	@Override
	public AuthenticationResponse authenticate(LoginRequest request) {

		Date expiredAt = new Date((new Date()).getTime() + 86400 * 1000);
		Authentication authentication = null;
		try {
			authentication = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		} catch (Exception e) {
			throw new AppException(e.getMessage());
		}
		if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))
				|| authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))) {
			String jwt = jwtService.generateToken(authentication);
			return new AuthenticationResponse(jwt, expiredAt.toInstant().toString());

		} else {
			throw new ForbiddenException("You do not have permission to access!");
		}
	}
}
