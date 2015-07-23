package io.corbel.lib.mongo.index;

import java.util.concurrent.TimeUnit;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class IndexOptions {

	private final BasicDBObject options;

	public IndexOptions() {
		options = new BasicDBObject();
	}

	public IndexOptions setSparse(boolean sparse) {
		options.put("sparse", sparse);
		return this;
	}

	public IndexOptions setBackground(boolean background) {
		options.put("background", background);
		return this;
	}

	public IndexOptions setUnique(boolean unique) {
		options.put("unique", unique);
		return this;
	}

	public IndexOptions setDropDups(boolean dropDups) {
		options.put("dropDups", dropDups);
		return this;
	}

	public IndexOptions setExpire(int expire, TimeUnit timeUnit) {
		options.put("expireAfterSeconds", timeUnit.toSeconds(expire));
		return this;
	}

	public IndexOptions setName(String name) {
		options.put("name", name);
		return this;
	}

	public DBObject getDBObject() {
		return options;
	}

	@Override
	public String toString() {
		return options.toString();
	}
}