package com.app.sockets;

import com.app.sockets.client.ChatsService;
import com.app.sockets.redis.RedisUserChannelService;
import com.app.sockets.repository.ChatMessageRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class SocketsApplicationTests {

	@MockitoBean
	private ChatMessageRepository chatMessageRepository;

	@MockitoBean
	private RedisUserChannelService redisUserChannelService;

	@MockitoBean
	private ChatsService chatsService;

	@Test
	void contextLoads() {
	}
}
