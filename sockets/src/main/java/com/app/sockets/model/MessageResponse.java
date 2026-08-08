package com.app.sockets.model;

import com.app.sockets.entity.ChatMessage;

import java.time.Instant;
import java.util.UUID;

public record MessageResponse(
		UUID messageId,
		UUID chatId,
		UUID senderId,
		String senderUsername,
		String content,
		Instant sentAt
) {

	public static MessageResponse from(ChatMessage message) {
		return new MessageResponse(
				message.getMessageId(),
				message.getChatId(),
				message.getSenderId(),
				message.getSenderUsername(),
				message.getContent(),
				message.getSentAt()
		);
	}

	public static MessageResponse from(ChatMessageEvent event) {
		return new MessageResponse(
				event.messageId(),
				event.chatId(),
				event.senderId(),
				event.senderUsername(),
				event.content(),
				event.sentAt()
		);
	}
}
