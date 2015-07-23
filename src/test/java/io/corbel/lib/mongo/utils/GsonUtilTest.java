package io.corbel.lib.mongo.utils;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;

import org.junit.Test;

import com.google.gson.JsonPrimitive;

/**
 * @author Rubén Carrasco
 *
 */
public class GsonUtilTest {

	@Test
	public void test() {

		Object date = GsonUtil.getPrimitive(new JsonPrimitive("ISODate(2014-09-10T19:29:55.123+02:00)"));
		assertThat(date).isExactlyInstanceOf(Date.class);

		date = GsonUtil.getPrimitive(new JsonPrimitive("ISODate(2014-09-10T19:29:55.123+02)"));
		assertThat(date).isExactlyInstanceOf(Date.class);

		date = GsonUtil.getPrimitive(new JsonPrimitive("ISODate(2014-09-10T19:29:55.123Z)"));
		assertThat(date).isExactlyInstanceOf(Date.class);
	}

	@Test
	public void testOffset() {
		Object date = GsonUtil.getPrimitive(new JsonPrimitive("ISODate(2014-09-10T19:29:55.123+02)"));
		assertThat(date).isEqualTo(GsonUtil.getPrimitive(new JsonPrimitive("ISODate(2014-09-10T19:29:55.123+02:00)")));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testWrongDate() {
		GsonUtil.getPrimitive(new JsonPrimitive("ISODate(2014-09-10T19:29:55)"));
	}

}
