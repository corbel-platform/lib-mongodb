package io.corbel.lib.mongo.index;

import java.util.concurrent.TimeUnit;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexDefinition;

import com.mongodb.DBObject;

/**
 * @author Alexander De Leon
 * 
 */
public class MongoIndex {

	private final Index index;
	private final IndexOptions options;

	public MongoIndex() {
		index = new Index();
		options = new IndexOptions();
	}

	public MongoIndex on(String key) {
		index.on(key, Direction.ASC);
		return this;
	}

	public MongoIndex on(String key, Direction direction) {
		index.on(key, direction);
		return this;
	}

	public MongoIndex named(String name) {
		options.setName(name);
		return this;
	}

	public MongoIndex unique() {
		options.setUnique(true);
		return this;
	}

	public MongoIndex sparse() {
		options.setSparse(true);
		return this;
	}

	public MongoIndex background() {
		options.setBackground(true);
		return this;
	}

	public MongoIndex dropDups() {
		options.setDropDups(true);
		return this;
	}

	public MongoIndex expires(int expire, TimeUnit timeUnit) {
		options.setExpire(expire, timeUnit);
		return this;
	}

	public IndexDefinition getIndexDefinition() {
		return new IndexDefinition() {

			@Override
			public DBObject getIndexOptions() {
				return options.getDBObject();
			}

			@Override
			public DBObject getIndexKeys() {
				return index.getIndexKeys();
			}
		};

	}

}
