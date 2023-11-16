package com.nexcode.websocket.model.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationResponse {

	private String type = "Bearer";
	private String token;
	private String expiredAt;

	public AuthenticationResponse(String token, String expiredAt) {
		this.token = token;
		this.expiredAt = expiredAt;
	}

}