package com.mall.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description: 创建统一的返回机制
 * @Date: 2018-07-30
 * @Time: 14:32
 */
@ToString
@Getter
@Setter
public class JsonData {

    private int status;

    private String msg;

    private Object data;

    public JsonData() {

    }

    public static JsonData success() {
        JsonData jsonData = new JsonData();
        return jsonData;
    }

    public static JsonData success(int status) {

        JsonData jsonData = new JsonData();
        jsonData.setStatus(status);
        return jsonData;
    }

    public static JsonData success(Object data) {
        JsonData jsonData = new JsonData();
        jsonData.setData(data);
        return jsonData;
    }

    public static JsonData success(int status, String msg, Object data) {
        JsonData jsonData = new JsonData();
        jsonData.setStatus(status);
        jsonData.setMsg(msg);
        jsonData.setData(data);

        return jsonData;
    }

    public static JsonData success(int status, String msg) {
        JsonData jsonData = new JsonData();
        jsonData.setStatus(status);
        jsonData.setMsg(msg);

        return jsonData;
    }

    public static JsonData fail(int status, String msg) {
        JsonData jsonData = new JsonData();
        jsonData.setStatus(status);

        jsonData.setMsg(msg);
        return jsonData;
    }
}
