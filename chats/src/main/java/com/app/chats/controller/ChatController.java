package com.app.chats.controller;

import com.app.chats.model.AddMemberRequest;
import com.app.chats.model.ChatResponse;
import com.app.chats.model.CreateChatRequest;
import com.app.chats.service.ChatService;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class ChatController {

	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ChatResponse create(@RequestBody CreateChatRequest request) {
		return chatService.create(request);
	}

	@GetMapping("/{chatId}")
	public ChatResponse get(@PathVariable UUID chatId) {
		return chatService.get(chatId);
	}

	@PostMapping("/{chatId}/members")
	public ChatResponse addMember(@PathVariable UUID chatId, @RequestBody AddMemberRequest request) {
		return chatService.addMember(chatId, request);
	}

	@GetMapping("/by-user/{userId}")
	public List<ChatResponse> listForUser(@PathVariable UUID userId) {
		return chatService.listForUser(userId);
	}
}
