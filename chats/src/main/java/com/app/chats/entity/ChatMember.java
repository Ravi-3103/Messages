package com.app.chats.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(
		name = "chat_members",
		uniqueConstraints = @UniqueConstraint(columnNames = {"chatId", "userId"})
)
public class ChatMember {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false)
	private UUID chatId;

	@Column(nullable = false)
	private UUID userId;

	@Column(nullable = false, updatable = false)
	private Instant joinedAt = Instant.now();

	protected ChatMember() {
	}

	public ChatMember(UUID chatId, UUID userId) {
		this.chatId = chatId;
		this.userId = userId;
	}

	public UUID getId() {
		return id;
	}

	public UUID getChatId() {
		return chatId;
	}

	public UUID getUserId() {
		return userId;
	}

	public Instant getJoinedAt() {
		return joinedAt;
	}
}
