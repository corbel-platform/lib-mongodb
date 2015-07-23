package io.corbel.lib.mongo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.DBObject;

/**
 * @author Alexander De Leon
 * 
 */
public class MongoCommonOperations {

	private static final String FIELD_ID = "_id";

	private MongoCommonOperations() {
	}

	public static String findStringFieldById(MongoOperations mongo, String field, String id, String collection) {
		Criteria criteria = Criteria.where(FIELD_ID).is(id);
		Query query = Query.query(criteria);
		query.fields().include(field);
		DBObject json = mongo.findOne(query, DBObject.class, collection);
		return json == null ? null : json.get(field).toString();
	}

	public static <T> boolean exists(MongoOperations mongo, Map<String, Object> fields, Class<T> clazz) {
		Query query = Query.query(getCriterias(fields));
		return mongo.exists(query, clazz);
	}

	private static Criteria getCriterias(Map<String, Object> fields) {
		List<Criteria> criterias = new ArrayList<Criteria>();
		for (Entry<String, Object> entry : fields.entrySet()) {
			criterias.add(Criteria.where(entry.getKey()).is(entry.getValue()));
		}
		switch (criterias.size()) {
			case 0:
				return new Criteria();
			case 1:
				return criterias.get(0);
			default:
				return new Criteria().andOperator(criterias.toArray(new Criteria[criterias.size()]));
		}
	}

	public static <T> void delete(MongoOperations mongo, Map<String, Object> fields, Class<T> clazz) {
		Query query = Query.query(getCriterias(fields));
		mongo.remove(query, clazz);
	}

}
