package com.app.sockets.config;

import com.app.security.JwtService;
import com.app.security.UserPrincipal;
import com.app.sockets.redis.RedisUserChannelService;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final JwtService jwtService;
	private final RedisUserChannelService redisUserChannelService;

	public WebSocketConfig(JwtService jwtService, RedisUserChannelService redisUserChannelService) {
		this.jwtService = jwtService;
		this.redisUserChannelService = redisUserChannelService;
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/ws")
				.setAllowedOriginPatterns("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.enableSimpleBroker("/topic");
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(new ChannelInterceptor() {
			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
				if (accessor == null) {
					return message;
				}

				if (StompCommand.CONNECT.equals(accessor.getCommand())) {
					String authHeader = accessor.getFirstNativeHeader("Authorization");
					if (authHeader == null || !authHeader.startsWith("Bearer ")) {
						throw new IllegalArgumentException("Missing Bearer token on CONNECT");
					}
					String token = authHeader.substring(7);
					if (!jwtService.isValid(token)) {
						throw new IllegalArgumentException("Invalid JWT on CONNECT");
					}
					UUID userId = jwtService.extractUserId(token);
					String username = jwtService.extractUsername(token);
					UserPrincipal principal = UserPrincipal.fromToken(userId, username);
					accessor.setUser(new UsernamePasswordAuthenticationToken(
							principal, null, principal.getAuthorities()));

					// Bind this sockets instance to the user's Redis channel
					redisUserChannelService.onUserConnected(userId);
					accessor.getSessionAttributes().put("userId", userId);
				}

				if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
					Object userIdAttr = accessor.getSessionAttributes() != null
							? accessor.getSessionAttributes().get("userId")
							: null;
					if (userIdAttr instanceof UUID userId) {
						redisUserChannelService.onUserDisconnected(userId);
					}
				}

				return message;
			}
		});
	}
}
