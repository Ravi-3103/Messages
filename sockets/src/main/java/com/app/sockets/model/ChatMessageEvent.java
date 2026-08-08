package com.app.sockets.model;

import java.time.Instant;
import java.util.UUID;

public record ChatMessageEvent(
		UUID messageId,
		UUID chatId,
		UUID senderId,
		String senderUsername,
		String content,
		Instant sentAt
) {
}
