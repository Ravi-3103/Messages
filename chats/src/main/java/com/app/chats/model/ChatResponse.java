package com.app.chats.model;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChatResponse(
		UUID id,
		String name,
		UUID createdByUserId,
		Instant createdAt,
		List<UUID> memberIds
) {
}
