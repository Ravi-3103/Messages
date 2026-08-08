package com.app.chats.model;

public record AuthResponse(String accessToken, String tokenType, UserResponse user) {

	public static AuthResponse bearer(String token, UserResponse user) {
		return new AuthResponse(token, "Bearer", user);
	}
}
