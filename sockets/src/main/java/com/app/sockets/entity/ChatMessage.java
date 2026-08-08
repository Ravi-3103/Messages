package com.app.sockets.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table("chat_messages")
public class ChatMessage {

	@PrimaryKey
	private ChatMessageKey key;

	@Column("sender_id")
	private UUID senderId;

	@Column("sender_username")
	private String senderUsername;

	@Column("content")
	private String content;

	protected ChatMessage() {
	}

	public ChatMessage(
			UUID chatId,
			Instant sentAt,
			UUID messageId,
			UUID senderId,
			String senderUsername,
			String content
	) {
		this.key = new ChatMessageKey(chatId, sentAt, messageId);
		this.senderId = senderId;
		this.senderUsername = senderUsername;
		this.content = content;
	}

	public ChatMessageKey getKey() {
		return key;
	}

	public UUID getChatId() {
		return key.getChatId();
	}

	public Instant getSentAt() {
		return key.getSentAt();
	}

	public UUID getMessageId() {
		return key.getMessageId();
	}

	public UUID getSenderId() {
		return senderId;
	}

	public String getSenderUsername() {
		return senderUsername;
	}

	public String getContent() {
		return content;
	}
}
