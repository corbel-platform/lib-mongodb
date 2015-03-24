package com.bq.oss.lib.mongo;

import static org.fest.assertions.api.Assertions.assertThat;

import java.time.Duration;
import java.time.Period;
import java.time.temporal.TemporalAmount;

import org.junit.Test;

/**
 * Created by Alberto J. Rubio
 */
public class StringToTemporalAmountConverterTest {


    @Test
    public void test() {
        StringToTemporalAmountConverter converter = new StringToTemporalAmountConverter();

        TemporalAmount temporalAmount = converter.convert("P01Y02M03D");
        assertThat(temporalAmount).isEqualTo(Period.of(1, 2, 3));

        temporalAmount = converter.convert("P10Y11M03DT00H00M00S");
        assertThat(temporalAmount).isEqualTo(Period.of(10, 11, 3));

        temporalAmount = converter.convert("P00Y00M00DT00H00M00S");
        assertThat(temporalAmount).isEqualTo(Period.of(0, 0, 0));

        temporalAmount = converter.convert("P00Y00M00DT00H00M00S");
        assertThat(temporalAmount).isEqualTo(Period.parse("P0M"));

        temporalAmount = converter.convert("P01Y03M00DT00H00M00S");
        assertThat(temporalAmount).isEqualTo(Period.parse("P1Y3M"));

        temporalAmount = converter.convert("P00Y00M00DT432000S");
        assertThat(temporalAmount).isEqualTo(Duration.ofDays(5));

        temporalAmount = converter.convert("P00Y00M00DT54000S");
        assertThat(temporalAmount).isEqualTo(Duration.ofHours(15));

        temporalAmount = converter.convert("P00Y00M00DT200M");
        assertThat(temporalAmount).isEqualTo(Duration.parse("PT3H20M"));

        temporalAmount = converter.convert("P00Y00M00DT15H00M00S");
        assertThat(temporalAmount).isEqualTo(Duration.parse("PT15H"));
    }
}
