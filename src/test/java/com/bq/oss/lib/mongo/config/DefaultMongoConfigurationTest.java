/*
 * Copyright (C) 2014 StarTIC
 */
package com.bq.oss.lib.mongo.config;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.core.env.Environment;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoServerSelectionException;

/**
 * @author Alexander De Leon
 *
 */
public class DefaultMongoConfigurationTest {

	protected static final String TEST_DB = "test";

	private static final String TEST_HOST_1 = "host1";
	private static final Integer TEST_PORT_1 = 11;
	private static final String TEST_HOST_2 = "host2";
	private static final Integer TEST_PORT_2 = 22;

	private static final String TEST_REPLICASET = TEST_HOST_1 + ":" + TEST_PORT_1 + ", " + TEST_HOST_2 + ":"
			+ TEST_PORT_2;

	private Environment environmentMock;
	private DefaultMongoConfiguration conf;

	@Before
	public void setup() {
		environmentMock = mock(Environment.class);
		conf = Mockito.spy(new DefaultMongoConfiguration() {

			@Override
			protected String getDatabaseName() {
				return TEST_DB;
			}

			@Override
			protected Environment getEnvironment() {
				return environmentMock;
			}
		});
	}

	@Test
	public void testHostAndPort() throws Exception {
		when(environmentMock.getProperty(TEST_DB + ".mongodb.host", String.class)).thenReturn(TEST_HOST_1);
		when(environmentMock.getProperty(TEST_DB + ".mongodb.port", Integer.class)).thenReturn(TEST_PORT_1);
		doReturn(MongoClientOptions.builder().build()).when(conf).getMongoOptions(); // empty options
		MongoClient client = null;
		try {
			client = conf.mongo();
		} catch (MongoServerSelectionException e) {
			// ignore connection error
		}

		assertThat(client.getAllAddress().get(0).getHost()).isEqualTo(TEST_HOST_1);
		assertThat(client.getAllAddress().get(0).getPort()).isEqualTo(TEST_PORT_1);
	}

	@Test
	public void testReplicateset() throws Exception {
		when(environmentMock.getProperty(TEST_DB + ".mongodb.replicaset", String.class)).thenReturn(TEST_REPLICASET);
		doReturn(MongoClientOptions.builder().build()).when(conf).getMongoOptions(); // empty options
		MongoClient client = null;
		try {
			client = conf.mongo();
		} catch (MongoServerSelectionException e) {
			// ignore connection error
		}

		assertThat(client.getAllAddress().get(0).getHost()).isEqualTo(TEST_HOST_1);
		assertThat(client.getAllAddress().get(0).getPort()).isEqualTo(TEST_PORT_1);
		assertThat(client.getAllAddress().get(1).getHost()).isEqualTo(TEST_HOST_2);
		assertThat(client.getAllAddress().get(1).getPort()).isEqualTo(TEST_PORT_2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBadConfig() throws Exception {
		doReturn(MongoClientOptions.builder().build()).when(conf).getMongoOptions(); // empty options
		try {
			conf.mongo();
		} catch (MongoServerSelectionException e) {
			// ignore connection error
		}
	}

	@Test
	public void testOkConfigWithoutHost() throws Exception {
		when(environmentMock.getProperty(TEST_DB + ".mongodb.host", String.class)).thenReturn(TEST_HOST_1);
		doReturn(MongoClientOptions.builder().build()).when(conf).getMongoOptions(); // empty options
		MongoClient client = null;
		try {
			client = conf.mongo();
		} catch (MongoServerSelectionException e) {
			// ignore connection error
		}

		assertThat(client.getAllAddress().get(0).getHost()).isEqualTo(TEST_HOST_1);
	}

}
