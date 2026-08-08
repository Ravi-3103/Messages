package com.app.sockets.redis;

import com.app.sockets.model.MessageResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisUserMessageSubscriber implements MessageListener {

	private static final Logger log = LoggerFactory.getLogger(RedisUserMessageSubscriber.class);

	private final ObjectMapper objectMapper;
	private final SimpMessagingTemplate messagingTemplate;

	public RedisUserMessageSubscriber(ObjectMapper objectMapper, SimpMessagingTemplate messagingTemplate) {
		this.objectMapper = objectMapper;
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onMessage(Message message, byte[] pattern) {
		try {
			String channel = new String(message.getChannel());
			MessageResponse payload = objectMapper.readValue(message.getBody(), MessageResponse.class);
			String userId = channel.substring(channel.indexOf(':') + 1);
			messagingTemplate.convertAndSend("/topic/users/" + userId, payload);
			log.debug("Pushed redis message on {} to /topic/users/{}", channel, userId);
		}
		catch (Exception ex) {
			log.error("Failed to handle redis pub/sub message", ex);
		}
	}
}
