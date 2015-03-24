package com.bq.oss.lib.mongo;

import org.springframework.core.convert.converter.Converter;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

/**
 * Created by Alberto J. Rubio
 */
public class StringToTemporalAmountConverter implements Converter<String, TemporalAmount> {

    private static final String PERIOD_REGEX = "00Y00M00D";
    private static final String DURATION_REGEX = "T00H00M00S";
    private static final String EMPTY = "";

    @Override
    public TemporalAmount convert(String object) {
        return object != null ? deserialize(object) : null;
    }

    private TemporalAmount deserialize(String object) {
        try {
            return Period.parse(object.replace(DURATION_REGEX, EMPTY));
        } catch (Exception e) {
            return Duration.parse(object.replace(PERIOD_REGEX, EMPTY));
        }
    }
}