package com.bq.oss.lib.mongo;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

import org.springframework.core.convert.converter.Converter;

public class TemporalAmountToStringConverter implements Converter<TemporalAmount, String> {

	private static final String ZERO_YEARS_MONTHS_AND_DAYS_PATTERN = "00Y00M00D";
	private static final String ZERO_HOURS_MINUTES_AND_SECONDS_PATTERN = "00H00M00S";

	private static final long SECONDS_PER_HOUR = 3600;
	private static final long SECONDS_PER_MINUTE = 60;

	@Override
	public String convert(TemporalAmount temporalAmount) {
		if (temporalAmount != null) {
			if (temporalAmount instanceof Period) {
				Period period = ((Period) temporalAmount).normalized();
				return new StringBuilder("P").append(normalizeString(period.getYears())).append("Y")
						.append(normalizeString(period.getMonths())).append("M")
						.append(normalizeString(period.getDays())).append("D").append("T")
						.append(ZERO_HOURS_MINUTES_AND_SECONDS_PATTERN).toString();
			} else {
				return convertDurationToString((Duration) temporalAmount);
			}
		}
		return null;
	}

	private String normalizeString(int amount) {
		return normalizeString(String.valueOf(amount));
	}

	private String normalizeString(String result) {
		if (result.length() == 1) {
			result = "0" + result;
		}
		return result;
	}

	private String convertDurationToString(Duration duration) {
		long hours = duration.getSeconds() / SECONDS_PER_HOUR;
		int minutes = (int) ((duration.getSeconds() % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
		int seconds = (int) (duration.getSeconds() % SECONDS_PER_MINUTE);
		return new StringBuilder("P").append(ZERO_YEARS_MONTHS_AND_DAYS_PATTERN).append("T").append(String.valueOf(hours))
				.append("H").append(normalizeString(minutes)).append("M").append(normalizeString(seconds)).append("S")
				.toString();
	}
}