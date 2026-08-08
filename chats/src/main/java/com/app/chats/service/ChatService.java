package com.app.chats.service;

import com.app.chats.entity.Chat;
import com.app.chats.entity.ChatMember;
import com.app.chats.model.AddMemberRequest;
import com.app.chats.model.ChatResponse;
import com.app.chats.model.CreateChatRequest;
import com.app.chats.repository.ChatMemberRepository;
import com.app.chats.repository.ChatRepository;
import com.app.security.SecurityUtils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {

	private final ChatRepository chatRepository;
	private final ChatMemberRepository chatMemberRepository;
	private final UserService userService;

	public ChatService(
			ChatRepository chatRepository,
			ChatMemberRepository chatMemberRepository,
			UserService userService
	) {
		this.chatRepository = chatRepository;
		this.chatMemberRepository = chatMemberRepository;
		this.userService = userService;
	}

	@Transactional
	public ChatResponse create(CreateChatRequest request) {
		if (request.name() == null || request.name().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "name is required");
		}
		UUID createdByUserId = SecurityUtils.currentUserId();
		userService.require(createdByUserId);

		Chat chat = chatRepository.save(new Chat(request.name().trim(), createdByUserId));
		chatMemberRepository.save(new ChatMember(chat.getId(), createdByUserId));
		return toResponse(chat);
	}

	@Transactional
	public ChatResponse addMember(UUID chatId, AddMemberRequest request) {
		if (request.userId() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
		}
		Chat chat = requireChat(chatId);
		userService.require(request.userId());

		if (chatMemberRepository.existsByChatIdAndUserId(chatId, request.userId())) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "user already a member");
		}
		chatMemberRepository.save(new ChatMember(chatId, request.userId()));
		return toResponse(chat);
	}

	@Transactional(readOnly = true)
	public ChatResponse get(UUID chatId) {
		return toResponse(requireChat(chatId));
	}

	@Transactional(readOnly = true)
	public List<ChatResponse> listForUser(UUID userId) {
		userService.require(userId);
		return chatMemberRepository.findByUserId(userId).stream()
				.map(member -> requireChat(member.getChatId()))
				.map(this::toResponse)
				.toList();
	}

	private Chat requireChat(UUID chatId) {
		return chatRepository.findById(chatId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "chat not found"));
	}

	private ChatResponse toResponse(Chat chat) {
		List<UUID> memberIds = chatMemberRepository.findByChatId(chat.getId()).stream()
				.map(ChatMember::getUserId)
				.toList();
		return new ChatResponse(
				chat.getId(),
				chat.getName(),
				chat.getCreatedByUserId(),
				chat.getCreatedAt(),
				memberIds
		);
	}
}
