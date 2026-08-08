package com.app.sockets.service;

import com.app.security.UserPrincipal;
import com.app.sockets.client.ChatsService;
import com.app.sockets.model.ChatMessageEvent;
import com.app.sockets.model.MessageResponse;
import com.app.sockets.model.SendMessagePayload;
import com.app.sockets.redis.RedisUserChannelService;

import java.security.Principal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.messaging.MessagingException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class MessageSendService {

	private final MessagePersistenceService messagePersistenceService;
	private final RedisUserChannelService redisUserChannelService;
	private final ChatsService chatsService;

	public MessageSendService(
			MessagePersistenceService messagePersistenceService,
			RedisUserChannelService redisUserChannelService,
			ChatsService chatsService
	) {
		this.messagePersistenceService = messagePersistenceService;
		this.redisUserChannelService = redisUserChannelService;
		this.chatsService = chatsService;
	}

	public void send(UUID chatId, SendMessagePayload payload, Principal principal) {
		UserPrincipal sender = requireUser(principal);
		if (payload == null || payload.content() == null || payload.content().isBlank()) {
			throw new MessagingException("content is required");
		}

		ChatMessageEvent event = new ChatMessageEvent(
				UUID.randomUUID(),
				chatId,
				sender.getId(),
				sender.getUsername(),
				payload.content().trim(),
				Instant.now()
		);

		messagePersistenceService.persist(event);

		MessageResponse response = MessageResponse.from(event);
		List<UUID> members = chatsService.membersOf(chatId);
		for (UUID memberId : members) {
			redisUserChannelService.publishToUser(memberId, response);
		}
	}

	private UserPrincipal requireUser(Principal principal) {
		if (principal instanceof Authentication authentication
				&& authentication.getPrincipal() instanceof UserPrincipal userPrincipal) {
			return userPrincipal;
		}
		throw new MessagingException("Not authenticated — connect with JWT first");
	}
}
