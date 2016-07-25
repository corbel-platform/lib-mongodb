package io.corbel.lib.mongo.index;

import org.springframework.data.mongodb.core.index.IndexDefinition;
import org.springframework.data.mongodb.core.index.TextIndexDefinition;

/**
 * @author Alberto J. Rubio
 * 
 */
public class MongoTextSearchIndex {

	private final TextIndexDefinition.TextIndexDefinitionBuilder index;

	public MongoTextSearchIndex() {
		index = new TextIndexDefinition.TextIndexDefinitionBuilder();
	}

	public MongoTextSearchIndex named(String name) {
		index.named(name);
		return this;
	}

	public MongoTextSearchIndex on(String... key) {
		index.onFields(key);
		return this;
	}

	public MongoTextSearchIndex on(String key, float weight) {
		index.onField(key, weight);
		return this;
	}

	public MongoTextSearchIndex onAllFields() {
		index.onAllFields();
		return this;
	}

	public MongoTextSearchIndex defaultLanguage(String language) {
		index.withDefaultLanguage(language);
		return this;
	}

	public MongoTextSearchIndex withLanguageOverride(String language) {
		index.withLanguageOverride(language);
		return this;
	}

	public IndexDefinition getIndexDefinition() {
		return index.build();
	}

}
