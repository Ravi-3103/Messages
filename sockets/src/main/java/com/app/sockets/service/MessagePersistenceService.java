package com.app.sockets.service;

import com.app.sockets.entity.ChatMessage;
import com.app.sockets.model.ChatMessageEvent;
import com.app.sockets.model.MessageResponse;
import com.app.sockets.repository.ChatMessageRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class MessagePersistenceService {

	private final ChatMessageRepository chatMessageRepository;

	public MessagePersistenceService(ChatMessageRepository chatMessageRepository) {
		this.chatMessageRepository = chatMessageRepository;
	}

	public void persist(ChatMessageEvent event) {
		ChatMessage message = new ChatMessage(
				event.chatId(),
				event.sentAt(),
				event.messageId(),
				event.senderId(),
				event.senderUsername(),
				event.content()
		);
		chatMessageRepository.save(message);
	}

	public List<MessageResponse> history(UUID chatId, int limit) {
		int pageSize = Math.min(Math.max(limit, 1), 100);
		return chatMessageRepository.findByKeyChatId(chatId, PageRequest.of(0, pageSize)).stream()
				.map(MessageResponse::from)
				.toList();
	}
}
