package com.app.chats.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "chats")
public class Chat {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@Column(nullable = false, length = 128)
	private String name;

	@Column(nullable = false)
	private UUID createdByUserId;

	@Column(nullable = false, updatable = false)
	private Instant createdAt = Instant.now();

	protected Chat() {
	}

	public Chat(String name, UUID createdByUserId) {
		this.name = name;
		this.createdByUserId = createdByUserId;
	}

	public UUID getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public UUID getCreatedByUserId() {
		return createdByUserId;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}
