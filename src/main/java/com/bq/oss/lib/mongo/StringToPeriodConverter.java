package com.bq.oss.lib.mongo;

import java.time.Period;

import org.springframework.core.convert.converter.Converter;

public class StringToPeriodConverter implements Converter<String, Period> {

	@Override
	public Period convert(String object) {
		return object != null ? Period.parse(object) : null;
	}
}