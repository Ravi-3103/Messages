package com.app.sockets.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyClass;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;

@PrimaryKeyClass
public class ChatMessageKey implements Serializable {

	@PrimaryKeyColumn(name = "chat_id", type = PrimaryKeyType.PARTITIONED, ordinal = 0)
	private UUID chatId;

	@PrimaryKeyColumn(name = "sent_at", type = PrimaryKeyType.CLUSTERED, ordinal = 1, ordering = Ordering.DESCENDING)
	private Instant sentAt;

	@PrimaryKeyColumn(name = "message_id", type = PrimaryKeyType.CLUSTERED, ordinal = 2)
	private UUID messageId;

	protected ChatMessageKey() {
	}

	public ChatMessageKey(UUID chatId, Instant sentAt, UUID messageId) {
		this.chatId = chatId;
		this.sentAt = sentAt;
		this.messageId = messageId;
	}

	public UUID getChatId() {
		return chatId;
	}

	public Instant getSentAt() {
		return sentAt;
	}

	public UUID getMessageId() {
		return messageId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof ChatMessageKey that)) {
			return false;
		}
		return Objects.equals(chatId, that.chatId)
				&& Objects.equals(sentAt, that.sentAt)
				&& Objects.equals(messageId, that.messageId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(chatId, sentAt, messageId);
	}
}
