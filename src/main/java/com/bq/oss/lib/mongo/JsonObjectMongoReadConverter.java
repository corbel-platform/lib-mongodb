/*
 * Copyright (C) 2013 StarTIC
 */
package com.bq.oss.lib.mongo;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.BasicDBList;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.springframework.core.convert.converter.Converter;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author Alexander De Leon
 *
 */
public class JsonObjectMongoReadConverter implements Converter<DBObject, JsonObject> {

    private final Gson gson;

    public JsonObjectMongoReadConverter(Gson gson) {
        this.gson = gson;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public JsonObject convert(DBObject source) {
        if (source == null) {
            return null;
        }
        return (JsonObject) gson.toJsonTree(rewriteMapFields(source.toMap()));
    }

    private Map<String, Object> rewriteMapFields(Map<String, Object> map) {
        Map<String, Object> returnMap = new HashMap<>();

        map.forEach(new BiConsumer<String, Object>() {
            @Override
            public void accept(String key, Object value) {
                String originalKey = SafeKeys.getOriginalKey(key);

                if (value instanceof Date) {
                    returnMap.put(originalKey, ((Date) value).getTime());
                } else if (value instanceof Map) {
                    returnMap.put(originalKey, rewriteMapFields((Map<String, Object>) value));
                } else if (value instanceof ObjectId) {
                    returnMap.put(originalKey, value.toString());
                } else if (value instanceof BasicDBList) {
                    returnMap.put(originalKey, rewriteArrayFields((List) value));
                }
                else {
                    returnMap.put(originalKey, value);
                }
            }
        });
        return returnMap;
    }

    private List rewriteArrayFields(List list) {
        List returnList = new ArrayList();
        list.stream().forEach((Object item) -> {
            if (item instanceof Map) {
                returnList.add(rewriteMapFields((Map) item));
            } else if (item instanceof BasicDBList) {
                returnList.add(rewriteArrayFields((List) item));
            } else {
                returnList.add(item);
            }
        });
        return returnList;
    }


}
