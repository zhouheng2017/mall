package com.mongo.dao;

import com.mongodb.util.JSON;

import java.util.Map;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 14:06
 */
public class JsonStrToMap {

    public static Map<String, Object> jsonStrToMap(String jsonString) {
        Object parseObj = JSON.parse(jsonString);
        //language=JSON

        Map<String, Object> map = (Map<String, Object>) parseObj;

        return map;
    }

    public static void main(String[] args) {
        String p = "{\"name\":\"zhouheng\", \"age\": \"20\"}";
        Object parse = JSON.parse(p);
        Map<String, String> map = (Map<String, String>) parse;

        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            System.out.println(stringStringEntry.getKey() + stringStringEntry.getValue());

        }

    }

}
