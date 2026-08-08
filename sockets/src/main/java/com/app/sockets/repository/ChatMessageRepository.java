package com.app.sockets.repository;

import com.app.sockets.entity.ChatMessage;
import com.app.sockets.entity.ChatMessageKey;

import java.util.List;
import java.util.UUID;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;

public interface ChatMessageRepository extends CassandraRepository<ChatMessage, ChatMessageKey> {

	List<ChatMessage> findByKeyChatId(UUID chatId, Pageable pageable);
}
