package com.mongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 13:29
 */
public class MongoHelper {
    static final String DBName = "zhouheng";
    static final String ServerAddres = "127.0.0.1";
    static final int port = 27017;

    public MongoHelper() {
    }

    public static MongoClient getMongoClient() {
        MongoClient mongoClient = null;

        try {
            mongoClient = new MongoClient(ServerAddres, port);
            System.out.println("connect to mongodb successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongoClient;
    }

    public static MongoDatabase getMongoDataBase(MongoClient mongoClient) {

        MongoDatabase mongoDatabase = null;

        try {
            if (mongoClient != null) {
                mongoDatabase = mongoClient.getDatabase(DBName);
                System.out.println("connect to DataBase successfully");

            } else {
                throw new RuntimeException("MongoClient 不能为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mongoDatabase;
    }

    public static void closeMongoClient(MongoDatabase mongoDatabase, MongoClient mongoClient) {
        if (mongoDatabase != null) {
            mongoDatabase = null;

        }
        if (mongoClient != null) {
            mongoClient.close();

        }
        System.out.println("closetMongoClient successfully");

    }

    public static void main(String[] args) {
        MongoClient mongoClient = getMongoClient();
        MongoDatabase mongoDataBase = getMongoDataBase(mongoClient);

    }
}


















