package com.nexcode.websocket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexcode.websocket.model.request.LoginRequest;
import com.nexcode.websocket.model.response.AuthenticationResponse;
import com.nexcode.websocket.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/user/login")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
}
