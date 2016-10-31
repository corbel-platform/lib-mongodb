package io.corbel.lib.mongo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;

import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;

/**
 * @author Alexander De Leon
 * 
 */
public class IdGeneratorMongoEventListener<E> extends AbstractMongoEventListener<E> {

	private static final Logger LOG = LoggerFactory.getLogger(IdGeneratorMongoEventListener.class);

	private static final String _ID = "_id";

	private final IdGenerator<E> generator;

	private final Class<?> domainClass;

	public IdGeneratorMongoEventListener(IdGenerator<E> generator, Class<E> domainClass) {
		this.generator = generator;
		this.domainClass = domainClass;
	}

	@Override
	public void onBeforeSave(BeforeSaveEvent<E> event) {
		E source = event.getSource();
		if (matchDomainClass(source)) {
			DBObject dbo = event.getDBObject();
			if (!dbo.containsField(_ID) || dbo.get(_ID) == null) {
				String generateId = generator.generateId(source);
				LOG.debug("Generated _id for {}: {}", source, generateId);
				dbo.put(_ID, generateId);
			}
		}
		super.onBeforeSave(event);
	}

	private boolean matchDomainClass(E source) {
		return domainClass.isAssignableFrom(source.getClass());
	}

}
