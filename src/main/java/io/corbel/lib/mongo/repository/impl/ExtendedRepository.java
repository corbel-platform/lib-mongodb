package io.corbel.lib.mongo.repository.impl;

import java.io.Serializable;
import java.util.Set;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.util.Assert;

import io.corbel.lib.mongo.repository.PartialUpdateRepository;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * @author Alexander De Leon
 * 
 */
public class ExtendedRepository<E, ID extends Serializable> extends SimpleMongoRepository<E, ID> implements
		PartialUpdateRepository<E, ID> {

	private final MongoOperations mongoOperations;
	private final MongoEntityInformation<E, ID> metadata;

	public ExtendedRepository(MongoEntityInformation<E, ID> metadata, MongoOperations mongoOperations) {
		super(metadata, mongoOperations);
		this.mongoOperations = mongoOperations;
		this.metadata = metadata;
	}

	@Override
	public boolean patch(ID id, E data, String... fieldsToDelete) {
		DBObject dbObject = getDbObject(data);
		return doUpdate(mongoOperations.getConverter().convertToMongoType(id), dbObject, fieldsToDelete);
	}

	@Override
	public boolean patch(E data, String... fieldsToDelete) {
		DBObject dbObject = getDbObject(data);
		Object id = dbObject.get("_id");
		return doUpdate(id, dbObject, fieldsToDelete);
	}

	private DBObject getDbObject(E data) {
		Object mongoObject = mongoOperations.getConverter().convertToMongoType(data);
		if (!(mongoObject instanceof DBObject)) {
			throw new IllegalArgumentException("Object of type " + data.getClass() + " cannot be converted to DBObject");
		}
		DBObject dbObject = (DBObject) mongoObject;
		return dbObject;
	}

	private boolean doUpdate(Object id, DBObject data, String... fieldsToDelete) {
		Assert.notNull(id);
		data.removeField("_id");

		Update update = new Update();
		setField("", data, update);
		for (String field : fieldsToDelete) {
			update.unset(field);
		}

		WriteResult result = mongoOperations.updateFirst(Query.query(Criteria.where("_id").is(id)), update,
				metadata.getCollectionName());
		return result.getN() != 0;
	}

	private void setField(String fieldPrefix, DBObject data, Update update) {
		Set<String> keySet = data.keySet();
		for (String field : keySet) {
			Object value = data.get(field);
			if (value instanceof DBObject) {
				setField(field + ".", (DBObject) value, update);
			} else {
				update.set(fieldPrefix + field, value);
			}
		}
	}
}
