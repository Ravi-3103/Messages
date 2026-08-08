package com.app.chats.controller;

import com.app.chats.model.UserResponse;
import com.app.security.SecurityUtils;
import com.app.chats.service.UserService;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService userService;

	public UserController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/me")
	public UserResponse me() {
		return userService.get(SecurityUtils.currentUserId());
	}

	@GetMapping("/{id}")
	public UserResponse get(@PathVariable UUID id) {
		return userService.get(id);
	}
}
