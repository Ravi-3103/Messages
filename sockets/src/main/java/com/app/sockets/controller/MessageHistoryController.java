package com.app.sockets.controller;

import com.app.sockets.model.MessageResponse;
import com.app.sockets.service.MessagePersistenceService;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chats")
public class MessageHistoryController {

	private final MessagePersistenceService messagePersistenceService;

	public MessageHistoryController(MessagePersistenceService messagePersistenceService) {
		this.messagePersistenceService = messagePersistenceService;
	}

	@GetMapping("/{chatId}/messages")
	public List<MessageResponse> history(
			@PathVariable UUID chatId,
			@RequestParam(defaultValue = "50") int limit
	) {
		return messagePersistenceService.history(chatId, limit);
	}
}
