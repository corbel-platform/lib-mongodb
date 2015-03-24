/*
 * Copyright (C) 2013 StarTIC
 */
package com.bq.oss.lib.mongo;

import java.util.Map.Entry;

import com.bq.oss.lib.mongo.utils.GsonUtil;
import com.google.gson.*;
import com.google.gson.internal.LazilyParsedNumber;
import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author Alexander De Leon
 * 
 */
public class JsonObjectMongoWriteConverter implements Converter<JsonElement, DBObject> {
	private static final String JSON_DECIMAL_SEPARATOR = ".";

	@Override
	public DBObject convert(JsonElement source) {
		if (source == null || source.isJsonNull()) {
			return null;
		}
		if (source.isJsonObject()) {
			return convert((JsonObject) source);
		}
		return convert((JsonArray) source);
	}

	private DBObject convert(JsonObject source) {
		DBObject object = new BasicDBObject();
		for (Entry<String, JsonElement> entry : source.entrySet()) {
			String key = SafeKeys.getSafeKey(entry.getKey());

			if (entry.getValue().isJsonObject()) {
				object.put(key, convert(entry.getValue().getAsJsonObject()));
			} else if (entry.getValue().isJsonArray()) {
				object.put(key, convert(entry.getValue().getAsJsonArray()));
			} else if (entry.getValue().isJsonPrimitive()) {
				object.put(key, GsonUtil.getPrimitive(parseJsonPrimitive(entry.getValue())));
			}
		}
		return object;
	}



	private DBObject convert(JsonArray array) {
		BasicDBList list = new BasicDBList();
		for (JsonElement element : array) {
			if (element.isJsonArray()) {
				list.add(convert(element.getAsJsonArray()));
			} else if (element.isJsonObject()) {
				list.add(convert(element.getAsJsonObject()));
			} else if (element.isJsonPrimitive()) {
				list.add(GsonUtil.getPrimitive(parseJsonPrimitive(element)));
			}
		}
		return list;
	}

	private JsonPrimitive parseJsonPrimitive (JsonElement primitive) {
		return convertIfLazilyParsedNumber(primitive.getAsJsonPrimitive());
	}

	private JsonPrimitive convertIfLazilyParsedNumber(JsonPrimitive primitive) {
		if (primitive.isNumber()) {
			Number number = primitive.getAsNumber();
			if (number instanceof LazilyParsedNumber) {
				if (number.toString().contains(JSON_DECIMAL_SEPARATOR)) {
					number = number.doubleValue();
				} else {
					number = number.longValue();
				}
				primitive = new JsonPrimitive(number);
			}
		}
		return primitive;
	}

}
