package com.app.chats.repository;

import com.app.chats.entity.ChatMember;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, UUID> {

	List<ChatMember> findByChatId(UUID chatId);

	List<ChatMember> findByUserId(UUID userId);

	Optional<ChatMember> findByChatIdAndUserId(UUID chatId, UUID userId);

	boolean existsByChatIdAndUserId(UUID chatId, UUID userId);
}
