package com.app.chats.repository;

import com.app.chats.entity.Chat;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, UUID> {
}
