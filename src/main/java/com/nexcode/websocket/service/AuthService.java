package com.nexcode.websocket.service;

import com.nexcode.websocket.model.request.LoginRequest;
import com.nexcode.websocket.model.response.AuthenticationResponse;

public interface AuthService {

	AuthenticationResponse authenticate(LoginRequest request);

}
