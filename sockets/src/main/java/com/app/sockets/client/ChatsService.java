package com.app.sockets.client;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ChatsService {

	private final RestClient restClient;

	public ChatsService(@Value("${app.chats.base-url}") String chatsBaseUrl) {
		this.restClient = RestClient.builder()
				.baseUrl(chatsBaseUrl)
				.build();
	}

	public List<UUID> membersOf(UUID chatId) {
		UUID[] members = restClient.get()
				.uri("/internal/chats/{chatId}/members", chatId)
				.retrieve()
				.body(UUID[].class);
		if (members == null) {
			return List.of();
		}
		return Arrays.asList(members);
	}
}
