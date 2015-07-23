package io.corbel.lib.mongo;

import org.junit.Test;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * Created by Alberto J. Rubio
 */
public class TemporalAmountToStringConverterTest {


    @Test
    public void test() {
        TemporalAmountToStringConverter converter = new TemporalAmountToStringConverter();

        TemporalAmount temporalAmount = Period.of(1, 2, 3);
        String temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P01Y02M03DT00H00M00S");

        temporalAmount = Period.of(10, 11, 3);
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P10Y11M03DT00H00M00S");

        temporalAmount = Period.of(0, 0, 0);
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P00Y00M00DT00H00M00S");

        temporalAmount = Period.parse("P0M");
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P00Y00M00DT00H00M00S");

        temporalAmount = Period.parse("P15M0D");
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P01Y03M00DT00H00M00S");

        temporalAmount = Duration.ofDays(5);
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P00Y00M00DT120H00M00S");

        temporalAmount = Duration.ofHours(15);
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P00Y00M00DT15H00M00S");

        temporalAmount = Duration.parse("PT200S");
        temporalAmountParsed = converter.convert(temporalAmount);
        assertThat(temporalAmountParsed).isEqualTo("P00Y00M00DT0H03M20S");

        assertThat(converter.convert(Period.parse("P1Y")).compareTo(converter.convert(Period.parse("P5M6D"))))
                .isEqualTo(1);

        assertThat(converter.convert(Period.parse("P1Y")).compareTo(converter.convert(Period.parse("P1Y"))))
                .isEqualTo(0);
        
		assertThat(converter.convert(Period.parse("P1Y")).compareTo(converter.convert(Duration.parse("PT100S"))))
                        .isEqualTo(1);
    }
}
