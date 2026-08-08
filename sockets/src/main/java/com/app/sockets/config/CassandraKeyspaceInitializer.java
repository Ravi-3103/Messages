package com.app.sockets.config;

import java.net.InetSocketAddress;

import com.datastax.oss.driver.api.core.CqlSession;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "app.cassandra.init-keyspace", havingValue = "true", matchIfMissing = true)
public class CassandraKeyspaceInitializer {

	@Value("${spring.cassandra.contact-points:127.0.0.1}")
	private String contactPoints;

	@Value("${spring.cassandra.port:9042}")
	private int port;

	@Value("${spring.cassandra.local-datacenter:datacenter1}")
	private String localDatacenter;

	@Value("${spring.cassandra.keyspace-name:messages}")
	private String keyspace;

	@PostConstruct
	public void createKeyspaceIfMissing() {
		try (CqlSession session = CqlSession.builder()
				.addContactPoint(new InetSocketAddress(contactPoints, port))
				.withLocalDatacenter(localDatacenter)
				.build()) {
			session.execute("""
					CREATE KEYSPACE IF NOT EXISTS %s
					WITH replication = {'class':'SimpleStrategy', 'replication_factor':1}
					""".formatted(keyspace));
		}
	}
}
