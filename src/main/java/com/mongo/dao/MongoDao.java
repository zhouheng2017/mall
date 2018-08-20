package com.mongo.dao;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Map;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 13:50
 */
public interface MongoDao {
    /**
     * Get Data by Id
     *
     * @param db    db
     * @param table table
     * @param id    id
     * @return
     * @throws Exception
     */
    Map<String, Object> queryById(MongoDatabase db, String table, Object id) throws Exception;

    /**
     * Insert Data
     *
     * @param db
     * @param table
     * @param document
     * @return
     */
    boolean insert(MongoDatabase db, String table, Document document);

    /**
     *  Delete Many Data.if doc is empty will delete all Data
     *
     * @param db
     * @param table
     * @param doc
     * @return
     */
    boolean delete(MongoDatabase db, String table, BasicDBObject doc);

    /**
     * update all data
     * @param db
     * @param table
     * @param oldDoc
     * @param newDoc
     * @return
     */
    boolean update(MongoDatabase db, String table, BasicDBObject oldDoc, BasicDBObject newDoc);



}
