package io.corbel.lib.mongo.repository.test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.corbel.lib.mongo.repository.impl.ExtendedRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;

/**
 * @author Alexander De Leon
 * 
 */
public class ExtendedRepositoryTest {

	private static final String TEST_COLLECTION = "collection";

	private ExtendedRepository<TestBean, String> repo;
	private MongoEntityInformation<TestBean, String> metadata;
	private MongoOperations mongoOperations;
	private MongoConverter mongoConverter;

	@Before
	public void setup() {
		metadata = mock(MongoEntityInformation.class);
		when(metadata.getCollectionName()).thenReturn(TEST_COLLECTION);
		when(metadata.getJavaType()).thenReturn(TestBean.class);
		when(metadata.getIdType()).thenReturn(String.class);
		mongoOperations = mock(MongoOperations.class);
		mongoConverter = mock(MongoConverter.class);
		when(mongoOperations.getConverter()).thenReturn(mongoConverter);
		repo = new ExtendedRepository<TestBean, String>(metadata, mongoOperations);
	}

	@Test
	public void testPatch() {
		TestBean bean = new TestBean();
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("_id", "id");
		dbObject.put("a", "A");
		when(mongoConverter.convertToMongoType(bean)).thenReturn(dbObject);
		ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
		ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

		WriteResult result = mock(WriteResult.class);
		when(result.getN()).thenReturn(1);
		when(mongoOperations.updateFirst(queryCaptor.capture(), updateCaptor.capture(), Mockito.eq(TEST_COLLECTION)))
				.thenReturn(result);

		boolean updated = repo.patch(bean);

		assertThat(updated).isTrue();
		assertThat(queryCaptor.getValue().getQueryObject().get("_id")).isEqualTo("id");
		assertThat(queryCaptor.getValue().getQueryObject().keySet().size()).isEqualTo(1);
		assertThat(updateCaptor.getValue().getUpdateObject().keySet().size()).isEqualTo(1);
		assertThat(((DBObject) updateCaptor.getValue().getUpdateObject().get("$set")).get("a")).isEqualTo("A");
	}

	@Test
	public void testPatchWithId() {
		TestBean bean = new TestBean();
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("_id", "id");
		dbObject.put("a", "A");
		when(mongoConverter.convertToMongoType(bean)).thenReturn(dbObject);
		ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
		ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

		WriteResult result = mock(WriteResult.class);
		when(result.getN()).thenReturn(1);
		when(mongoOperations.updateFirst(queryCaptor.capture(), updateCaptor.capture(), Mockito.eq(TEST_COLLECTION)))
				.thenReturn(result);

		String idReal = "idReal";
		when(mongoConverter.convertToMongoType(idReal)).thenReturn(idReal);
		boolean updated = repo.patch(idReal, bean);

		assertThat(updated).isTrue();
		assertThat(queryCaptor.getValue().getQueryObject().get("_id")).isEqualTo("idReal");
		assertThat(queryCaptor.getValue().getQueryObject().keySet().size()).isEqualTo(1);
		assertThat(updateCaptor.getValue().getUpdateObject().keySet().size()).isEqualTo(1);
		assertThat(((DBObject) updateCaptor.getValue().getUpdateObject().get("$set")).get("a")).isEqualTo("A");
	}

	@Test
	public void testPatchWithUnset() {
		TestBean bean = new TestBean();
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("_id", "id");
		dbObject.put("a", "A");
		when(mongoConverter.convertToMongoType(bean)).thenReturn(dbObject);
		ArgumentCaptor<Query> queryCaptor = ArgumentCaptor.forClass(Query.class);
		ArgumentCaptor<Update> updateCaptor = ArgumentCaptor.forClass(Update.class);

		WriteResult result = mock(WriteResult.class);
		when(result.getN()).thenReturn(1);
		when(mongoOperations.updateFirst(queryCaptor.capture(), updateCaptor.capture(), Mockito.eq(TEST_COLLECTION)))
				.thenReturn(result);

		boolean updated = repo.patch(bean, "b");

		assertThat(updated).isTrue();
		assertThat(queryCaptor.getValue().getQueryObject().get("_id")).isEqualTo("id");
		assertThat(queryCaptor.getValue().getQueryObject().keySet().size()).isEqualTo(1);
		assertThat(updateCaptor.getValue().getUpdateObject().keySet().size()).isEqualTo(2);
		assertThat(((DBObject) updateCaptor.getValue().getUpdateObject().get("$set")).get("a")).isEqualTo("A");
		assertThat(((DBObject) updateCaptor.getValue().getUpdateObject().get("$unset")).get("b")).isEqualTo(1);
	}
}
