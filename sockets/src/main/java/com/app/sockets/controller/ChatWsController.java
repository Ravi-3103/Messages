package com.app.sockets.controller;

import com.app.sockets.model.SendMessagePayload;
import com.app.sockets.service.MessageSendService;

import java.security.Principal;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class ChatWsController {

	private final MessageSendService messageSendService;

	public ChatWsController(MessageSendService messageSendService) {
		this.messageSendService = messageSendService;
	}

	@MessageMapping("/chats/{chatId}")
	public void send(
			@DestinationVariable UUID chatId,
			@Payload SendMessagePayload payload,
			Principal principal
	) {
		messageSendService.send(chatId, payload, principal);
	}
}
