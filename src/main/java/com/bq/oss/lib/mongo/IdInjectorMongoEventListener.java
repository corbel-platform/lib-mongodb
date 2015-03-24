package com.bq.oss.lib.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import com.google.gson.JsonObject;
import com.mongodb.DBObject;

/**
 * @author Rubén Carrasco
 * 
 */
public class IdInjectorMongoEventListener extends AbstractMongoEventListener<JsonObject> {

	private static final Logger LOG = LoggerFactory.getLogger(IdInjectorMongoEventListener.class);

	private static final String ID = "id";

	private static final String _ID = "_id";

	@Override
	public void onAfterSave(JsonObject source, DBObject dbo) {
		if (dbo.containsField(_ID)) {
			String id = String.valueOf(dbo.get(_ID));
			LOG.debug("Adding id {} to object after saving", id);
			source.addProperty(ID, id);
		}
		super.onAfterSave(source, dbo);
	}
}
