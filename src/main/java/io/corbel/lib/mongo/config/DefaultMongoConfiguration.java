package io.corbel.lib.mongo.config;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.mongodb.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.WriteResultChecking;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

import com.google.common.base.Joiner;
import org.springframework.util.*;

/**
 * @author Alexander De Leon
 * 
 */
@Configuration
public abstract class DefaultMongoConfiguration extends AbstractMongoConfiguration {

    @SuppressWarnings("unused")
    @Override
    @Bean
    public MongoClient mongo() throws Exception {
        if (getMongoProperty("mongodb.replicaset", String.class) != null) {
            return new MongoClient(getMongoReplicaset(), getMongoCredentials(), getMongoOptions());
        } else if (getMongoProperty("mongodb.host", String.class) != null) {
            return new MongoClient(new ServerAddress(getMongoProperty("mongodb.host", String.class), getMongoProperty(
                    "mongodb.port", Integer.class, 27017)), getMongoCredentials(), getMongoOptions());
        }
        throw new IllegalArgumentException("Missing mongodb host or replicaset configuration");
    }

	private List<ServerAddress> getMongoReplicaset() throws UnknownHostException {
		String replicasetConfig = getMongoProperty("mongodb.replicaset", String.class);
		if (replicasetConfig == null) {
			return null;
		}
		String[] servers = replicasetConfig.split(",");
		List<ServerAddress> serverAddresses = new ArrayList<ServerAddress>(servers.length);
		for (String server : servers) {
			String[] hostAndPort = server.trim().split(":");
			try {
				serverAddresses.add(new ServerAddress(hostAndPort[0], Integer.parseInt(hostAndPort[1])));
			} catch (NumberFormatException e) {
				throw new IllegalArgumentException("Invalid port for server " + hostAndPort[0], e);
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new IllegalArgumentException("Missing port for server" + hostAndPort[1], e);
			}
		}
		return serverAddresses;
	}

	@Override
	public MappingMongoConverter mappingMongoConverter() throws Exception {

		MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory()),
				mongoMappingContext());
		// remove _class
		converter.setTypeMapper(mongoTypeMapper());
		converter.setCustomConversions(customConversions());
		return converter;
	}

	protected DefaultMongoTypeMapper mongoTypeMapper() {
		return new DefaultMongoTypeMapper(null);
	}

	@Bean
	public List<MongoCredential> getMongoCredentials() {
		List<MongoCredential> credentials = new ArrayList<MongoCredential>();
		String user = getMongoProperty("mongodb.username", String.class);
		String password = getMongoProperty("mongodb.password", String.class);
		String authDatabase = getMongoProperty("mongodb.authenticationDatabase", String.class, "admin");
		if (StringUtils.hasLength(user) && StringUtils.hasLength(password)) {
			credentials.add(MongoCredential.createMongoCRCredential(user, authDatabase, password.toCharArray()));
		}
		return credentials;
	}

	@Bean
	public MongoClientOptions getMongoOptions() {
		MongoClientOptions.Builder optionsBuilder = MongoClientOptions.builder();
		optionsBuilder.autoConnectRetry(getMongoProperty("mongodb.autoConnectRetry", Boolean.class));
		optionsBuilder.connectionsPerHost(getMongoProperty("mongodb.connectionsPerHost", Integer.class));
		optionsBuilder.connectTimeout(getMongoProperty("mongodb.connectTimeout", Integer.class));
		optionsBuilder.cursorFinalizerEnabled(getMongoProperty("mongodb.cursorFinalizerEnabled", Boolean.class));

		optionsBuilder.writeConcern(getWriteConcern());

		optionsBuilder.maxAutoConnectRetryTime(getMongoProperty("mongodb.maxAutoConnectRetryTime", Long.class));
		optionsBuilder.maxWaitTime(getMongoProperty("mongodb.maxWaitTime", Integer.class));

		optionsBuilder.socketKeepAlive(getMongoProperty("mongodb.socketKeepAlive", Boolean.class));
		optionsBuilder.socketTimeout(getMongoProperty("mongodb.socketTimeout", Integer.class));
		optionsBuilder.threadsAllowedToBlockForConnectionMultiplier(getMongoProperty(
				"mongodb.threadsAllowedToBlockForConnectionMultiplier", Integer.class));

		if (getMongoProperty("mongodb.slaveOk", Boolean.class)) {
			optionsBuilder.readPreference(ReadPreference.secondaryPreferred());
		}
		return optionsBuilder.build();
	}

	public WriteConcern getWriteConcern() {
		Boolean fsync = getMongoProperty("mongodb.fsync", Boolean.class);
		Boolean j = getMongoProperty("mongodb.j", Boolean.class);
		Integer w = getMongoProperty("mongodb.w", Integer.class);
		if (w == null) {
			w = 1; // by default use safe write concern
		}
		Integer wtimeout = getMongoProperty("mongodb.wtimeout", Integer.class);
		return new WriteConcern(w, wtimeout, fsync, j);
	}

	@Override
	@Bean
	public MongoTemplate mongoTemplate() throws Exception {
		MongoTemplate template = super.mongoTemplate();
		template.setWriteResultChecking(getWriteResultChecking());
		return template;
	}

	private <E> E getMongoProperty(String property, Class<E> type) {
		return getMongoProperty(property, type, null);
	}

	private <E> E getMongoProperty(String property, Class<E> type, E def) {
		E value = getEnvironment().getProperty(
				Joiner.on(".").join(new String[] { getMongoConfigurationPrefix(), property }), type);
		return value == null ? def : value;
	}

	protected WriteResultChecking getWriteResultChecking() {
		return WriteResultChecking.EXCEPTION;
	}

	protected abstract Environment getEnvironment();

	protected String getMongoConfigurationPrefix() {
		return getDatabaseName();
	}
}
