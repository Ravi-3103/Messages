package com.app.chats.controller;

import com.app.chats.repository.ChatMemberRepository;
import com.app.chats.repository.ChatRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/internal/chats")
public class InternalChatController {

	private final ChatRepository chatRepository;
	private final ChatMemberRepository chatMemberRepository;

	public InternalChatController(ChatRepository chatRepository, ChatMemberRepository chatMemberRepository) {
		this.chatRepository = chatRepository;
		this.chatMemberRepository = chatMemberRepository;
	}

	@GetMapping("/{chatId}/members")
	public List<UUID> members(@PathVariable UUID chatId) {
		if (!chatRepository.existsById(chatId)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "chat not found");
		}
		return chatMemberRepository.findByChatId(chatId).stream()
				.map(member -> member.getUserId())
				.toList();
	}
}
