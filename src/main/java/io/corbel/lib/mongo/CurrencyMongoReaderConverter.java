package io.corbel.lib.mongo;

import java.util.Currency;

import org.springframework.core.convert.converter.Converter;

import com.mongodb.DBObject;

public class CurrencyMongoReaderConverter implements Converter<DBObject, Currency> {

	@Override
	public Currency convert(DBObject object) {
		return Currency.getInstance(String.valueOf(object.get("currencyCode")));
	}

}
