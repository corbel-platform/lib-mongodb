package com.bq.oss.lib.mongo;

import java.util.Currency;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class CurrencyMongoWriterConverter implements Converter<Currency, DBObject> {

	@Override
	public DBObject convert(Currency currency) {
		return new BasicDBObject("currencyCode", currency.getCurrencyCode());
	}

}
