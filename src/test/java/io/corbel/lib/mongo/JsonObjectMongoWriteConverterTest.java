package io.corbel.lib.mongo;

import static org.fest.assertions.api.Assertions.assertThat;

import com.google.gson.JsonSyntaxException;
import org.junit.Test;

import com.google.gson.JsonParser;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;

/**
 * @author Alexander De Leon
 * 
 */
public class JsonObjectMongoWriteConverterTest {

	@Test
	public void testSimpleString() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		assertThat(converter.convert(parser.parse("{\"a\":\"1\"}").getAsJsonObject()).get("a")).isEqualTo("1");
	}

	@Test
	public void testSimpleNumber() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		assertThat((converter.convert(parser.parse("{\"a\":1.1}").getAsJsonObject()).get("a"))).isEqualTo(1.1d);
	}

	@Test(expected = JsonSyntaxException.class)
	public void testSimpleNumberCommaNotationFail() {
		JsonParser parser = new JsonParser();
		parser.parse("{\"a\":1,1}");
	}

	@Test
	public void testNegativeSimpleNumber() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		assertThat((converter.convert(parser.parse("{\"a\":-1.1}").getAsJsonObject()).get("a"))).isEqualTo(-1.1d);
	}

	@Test
	public void testSimpleBoolean() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		assertThat(converter.convert(parser.parse("{\"a\":true}").getAsJsonObject()).get("a")).isEqualTo(true);
	}

	@Test
	public void testNested() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		assertThat(
				((DBObject) converter.convert(parser.parse("{\"a\":{\"b\":\"c\"}}").getAsJsonObject()).get("a"))
						.get("b")).isEqualTo("c");
	}

	@Test
	public void testArray() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		BasicDBList list = (BasicDBList) converter.convert(parser.parse("{\"a\":[{\"c\":1},\"b\",3]}").getAsJsonObject())
				.get("a");
		assertThat(list.get(1)).isEqualTo("b");
		assertThat((((DBObject) list.get(0)).get("c"))).isEqualTo(1l);
		assertThat(list.get(2)).isEqualTo(3l);
	}

	@Test
	public void testNestedArray() {
		JsonObjectMongoWriteConverter converter = new JsonObjectMongoWriteConverter();
		JsonParser parser = new JsonParser();
		BasicDBList list = (BasicDBList) converter.convert(parser.parse("{\"a\":[[\"b\"]]}").getAsJsonObject())
				.get("a");
		assertThat(list.get(0)).isInstanceOf(BasicDBList.class);
	}
}
