/*
 * Copyright (C) 2013 StarTIC
 */
package com.bq.oss.lib.mongo;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.mongodb.DBObject;

/**
 * @author Alexander De Leon
 * 
 */
public class IdGeneratorMongoEventListenerTest {

	private static final String _ID = "_id";

	private static final String TEST_ID = "an_id";

	private IdGenerator<DomainClass> generatorMock;
	private DomainClass entity;
	private IdGeneratorMongoEventListener<DomainClass> eventListener;

	@SuppressWarnings("unchecked")
	@Before
	public void setup() {
		entity = new DomainClass();
		generatorMock = mock(IdGenerator.class);
		when(generatorMock.generateId(entity)).thenReturn(TEST_ID);
		eventListener = new IdGeneratorMongoEventListener<DomainClass>(generatorMock, DomainClass.class);
	}

	@Test
	public void testNewObjectWithNoId() {
		DBObject dbObject = mock(DBObject.class);
		when(dbObject.containsField(_ID)).thenReturn(false);
		eventListener.onBeforeSave(entity, dbObject);
		verify(dbObject, Mockito.times(1)).put(_ID, TEST_ID);
	}

	@Test
	public void testNewObjectWithIdNull() {
		DBObject dbObject = mock(DBObject.class);
		when(dbObject.containsField(_ID)).thenReturn(true);
		when(dbObject.get(_ID)).thenReturn(null);
		eventListener.onBeforeSave(entity, dbObject);
		verify(dbObject, Mockito.times(1)).put(_ID, TEST_ID);
	}

	@Test
	public void testObjectWithId() {
		DBObject dbObject = mock(DBObject.class);
		when(dbObject.containsField(_ID)).thenReturn(true);
		when(dbObject.get(_ID)).thenReturn(TEST_ID);
		eventListener.onBeforeSave(entity, dbObject);
		verify(dbObject, Mockito.never()).put(_ID, TEST_ID);
	}

	private class DomainClass {
		// empty
	}
}
