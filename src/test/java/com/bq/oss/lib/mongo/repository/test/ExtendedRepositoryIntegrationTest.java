/*
 * Copyright (C) 2014 StarTIC
 */
package com.bq.oss.lib.mongo.repository.test;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bq.oss.lib.mongo.config.DefaultMongoConfiguration;
import com.bq.oss.lib.mongo.config.MongoCommonRepositoryFactoryBean;
import com.bq.oss.lib.mongo.repository.test.ExtendedRepositoryIntegrationTest.IoC;

/**
 * Use this test to verify repository using a local mongo database.
 * 
 * @author Alexander De Leon
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IoC.class)
@Ignore
public class ExtendedRepositoryIntegrationTest {

	private static final String TEST_ID = "id";

	@Autowired
	private TestRepo repo;

	@Before
	public void setup() {
		TestBean example = new TestBean();
		example.id = TEST_ID;
		repo.save(example);
	}

	@After
	public void cleanup() {
		repo.delete(TEST_ID);
	}

	@Test
	public void testUpdateOneField() {
		TestBean bean = new TestBean();
		bean.id = TEST_ID;
		bean.a = "a";
		repo.patch(bean);

		TestBean current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");

		bean.a = null;
		bean.b = 3;

		repo.patch(bean);

		current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");
		assertThat(current.b).isEqualTo(3);

	}

	@Test
	public void testUpdateOneFieldAndDeleteOther() {
		TestBean bean = new TestBean();
		bean.id = TEST_ID;
		bean.a = "a";
		repo.patch(bean);

		TestBean current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");

		bean.a = null;
		bean.b = 3;
		bean.c = "c";

		repo.patch(bean, "a");

		current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo(null);
		assertThat(current.b).isEqualTo(3);
		assertThat(current.c).isEqualTo("c");
	}

	@Test
	public void testUpdateEmbeded() {
		TestBean bean = new TestBean();
		TestBean embeded = new TestBean();
		bean.embeded = embeded;

		bean.id = TEST_ID;
		bean.a = "a";

		embeded.a = "e-a";

		repo.patch(bean);

		TestBean current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");
		assertThat(current.embeded.a).isEqualTo("e-a");

		bean.a = null;
		embeded.a = null;
		embeded.b = 10;

		repo.patch(bean);

		current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");
		assertThat(current.embeded.a).isEqualTo("e-a");
		assertThat(current.embeded.b).isEqualTo(10);
	}

	@Test
	public void testUpdateEmbededRemoveField() {
		TestBean bean = new TestBean();
		TestBean embeded = new TestBean();
		bean.embeded = embeded;

		bean.id = TEST_ID;
		bean.a = "a";

		embeded.a = "e-a";

		repo.patch(bean);

		TestBean current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");
		assertThat(current.embeded.a).isEqualTo("e-a");

		bean.a = null;
		embeded.a = null;
		embeded.b = 10;

		repo.patch(bean, "embeded.a");

		current = repo.findOne(TEST_ID);
		assertThat(current.a).isEqualTo("a");
		assertThat(current.embeded.a).isEqualTo(null);
		assertThat(current.embeded.b).isEqualTo(10);
	}

	@Configuration
	@PropertySource("classpath:/environment.properties")
	@EnableMongoRepositories(value = "com.bqreaders.silkroad.mongo.repository", repositoryFactoryBeanClass = MongoCommonRepositoryFactoryBean.class)
	public static class IoC extends DefaultMongoConfiguration {

		@Autowired
		Environment environment;

		@Override
		protected Environment getEnvironment() {
			return environment;
		}

		@Override
		protected String getDatabaseName() {
			return "test";
		}

	}

}
