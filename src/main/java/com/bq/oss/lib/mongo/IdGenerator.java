/*
 * Copyright (C) 2013 StarTIC
 */
package com.bq.oss.lib.mongo;

/**
 * @author Alexander De Leon
 * 
 */
public interface IdGenerator<E> {

	String generateId(E entity);

}
