package com.bq.oss.lib.mongo.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.format.ISODateTimeFormat;

import com.google.gson.JsonPrimitive;

public class GsonUtil {
	private static final Pattern ISO_DATE_PATTERN = Pattern.compile("^ISODate[(](.*?)[)]$");

	public static Object getPrimitive(JsonPrimitive primitive) {
		if (primitive.isBoolean()) {
			return primitive.getAsBoolean();
		} else if (primitive.isNumber()) {
			Number num = primitive.getAsNumber();
			if (num instanceof BigDecimal || num instanceof Double) {
				return num.doubleValue();
			} else if (num instanceof BigInteger || num instanceof Long) {
				return num.longValue();
			}
			return num;
		} else if (primitive.isString()) {
			String value = primitive.getAsString();
			Matcher isoDateMatcher = ISO_DATE_PATTERN.matcher(value);
			if (isoDateMatcher.find()) {
				return ISODateTimeFormat.dateTime().parseDateTime(isoDateMatcher.group(1)).toDate();
			}
			return value;
		}
		return null;
	}
}
