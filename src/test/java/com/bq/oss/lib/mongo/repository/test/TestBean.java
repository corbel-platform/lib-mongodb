/*
 * Copyright (C) 2014 StarTIC
 */
package com.bq.oss.lib.mongo.repository.test;

import org.springframework.data.annotation.Id;

/**
 * @author Alexander De Leon
 */
public class TestBean {

	public String a;

	public Integer b;

	public String c;

	public TestBean embeded;

	@Id
	public String id;
}