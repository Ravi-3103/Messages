package com.app.sockets.redis;

import com.app.sockets.model.MessageResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Service
public class RedisUserChannelService {

	private static final Logger log = LoggerFactory.getLogger(RedisUserChannelService.class);

	private final RedisMessageListenerContainer listenerContainer;
	private final RedisUserMessageSubscriber subscriber;
	private final StringRedisTemplate stringRedisTemplate;
	private final ObjectMapper objectMapper;
	private final RedisChannelNames channelNames;

	/** Local connection count per user on this instance. */
	private final Map<UUID, AtomicInteger> localConnections = new ConcurrentHashMap<>();

	public RedisUserChannelService(
			RedisMessageListenerContainer listenerContainer,
			RedisUserMessageSubscriber subscriber,
			StringRedisTemplate stringRedisTemplate,
			ObjectMapper objectMapper,
			RedisChannelNames channelNames
	) {
		this.listenerContainer = listenerContainer;
		this.subscriber = subscriber;
		this.stringRedisTemplate = stringRedisTemplate;
		this.objectMapper = objectMapper;
		this.channelNames = channelNames;
	}

	public void onUserConnected(UUID userId) {
		AtomicInteger count = localConnections.computeIfAbsent(userId, id -> new AtomicInteger(0));
		if (count.getAndIncrement() == 0) {
			String channel = channelNames.userChannel(userId);
			listenerContainer.addMessageListener(subscriber, new ChannelTopic(channel));
			log.info("Subscribed this sockets instance to redis channel {}", channel);
		}
	}

	public void onUserDisconnected(UUID userId) {
		AtomicInteger count = localConnections.get(userId);
		if (count == null) {
			return;
		}
		if (count.decrementAndGet() <= 0) {
			localConnections.remove(userId);
			String channel = channelNames.userChannel(userId);
			listenerContainer.removeMessageListener(subscriber, new ChannelTopic(channel));
			log.info("Unsubscribed this sockets instance from redis channel {}", channel);
		}
	}

	public void publishToUser(UUID userId, MessageResponse payload) {
		try {
			String channel = channelNames.userChannel(userId);
			stringRedisTemplate.convertAndSend(channel, objectMapper.writeValueAsString(payload));
		}
		catch (JsonProcessingException ex) {
			throw new IllegalStateException("Failed to serialize message for redis", ex);
		}
	}
}
