package com.app.chats.service;

import com.app.chats.entity.User;
import com.app.chats.model.AuthResponse;
import com.app.chats.model.LoginRequest;
import com.app.chats.model.RegisterRequest;
import com.app.chats.model.UserResponse;
import com.app.chats.repository.UserRepository;
import com.app.security.JwtService;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;

	public AuthService(
			UserRepository userRepository,
			PasswordEncoder passwordEncoder,
			JwtService jwtService
	) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}

	public AuthResponse register(RegisterRequest request) {
		validateRegister(request);
		if (userRepository.existsByUsername(request.username().trim())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
		}

		User user = userRepository.save(new User(
				request.username().trim(),
				request.displayName().trim(),
				passwordEncoder.encode(request.password())
		));

		String token = jwtService.generateToken(user.getId(), user.getUsername());
		return AuthResponse.bearer(token, UserResponse.from(user));
	}

	public AuthResponse login(LoginRequest request) {
		if (request.username() == null || request.username().isBlank()
				|| request.password() == null || request.password().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username and password are required");
		}

		User user = userRepository.findByUsername(request.username().trim())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials"));

		if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
		}

		String token = jwtService.generateToken(user.getId(), user.getUsername());
		return AuthResponse.bearer(token, UserResponse.from(user));
	}

	private void validateRegister(RegisterRequest request) {
		if (request.username() == null || request.username().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username is required");
		}
		if (request.displayName() == null || request.displayName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "displayName is required");
		}
		if (request.password() == null || request.password().length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "password must be at least 6 characters");
		}
	}
}
