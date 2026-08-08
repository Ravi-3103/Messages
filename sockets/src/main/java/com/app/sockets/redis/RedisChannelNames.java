package com.app.sockets.redis;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RedisChannelNames {

	private final String userChannelPrefix;

	public RedisChannelNames(@Value("${app.redis.user-channel-prefix:user:}") String userChannelPrefix) {
		this.userChannelPrefix = userChannelPrefix;
	}

	public String userChannel(UUID userId) {
		return userChannelPrefix + userId;
	}
}
