/*
 * Copyright (C) 2013 StarTIC
 */
package com.bq.oss.lib.mongo;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.google.gson.JsonPrimitive;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author Alexander De Leon
 * 
 */
public class JsonObjectMongoReadConverterTest {

	private static final Gson GSON = new Gson();
	private JsonObjectMongoReadConverter converter;

	@Before
	public void setup() {
		converter = new JsonObjectMongoReadConverter(GSON);
	}

	@Test
	public void testSimpleObject() {
		DBObject source = new BasicDBObject();
		source.put("a", "1");
		assertThat(converter.convert(source).get("a")).isEqualTo(new JsonPrimitive("1"));
	}

	@Test
	public void testSimpleObjectWithNumber() {
		DBObject source = new BasicDBObject();
		source.put("a", 1);
		assertThat(converter.convert(source).get("a")).isEqualTo(new JsonPrimitive(1));
	}

	@Test
	public void testNestedObject() {
		DBObject source = new BasicDBObject();
		source.put("a", "1");
		DBObject root = new BasicDBObject();
		root.put("child", source);
		assertThat(converter.convert(root).get("child").getAsJsonObject().get("a")).isEqualTo(new JsonPrimitive("1"));
	}

	@Test
	public void testSimpleObjectWithArray() {
		DBObject source = new BasicDBObject();
		source.put("a", Arrays.asList("1", "2"));
		assertThat(converter.convert(source).get("a").getAsJsonArray().get(0)).isEqualTo(new JsonPrimitive("1"));
		assertThat(converter.convert(source).get("a").getAsJsonArray().get(1)).isEqualTo(new JsonPrimitive("2"));
	}
}
