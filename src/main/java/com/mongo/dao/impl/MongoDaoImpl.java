package com.mongo.dao.impl;

import com.mongo.dao.JsonStrToMap;
import com.mongo.dao.MongoDao;
import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 13:56
 */
public class MongoDaoImpl implements MongoDao {
    /**
     * Get Data by Id
     *
     * @param db    db
     * @param table table
     * @param id    id
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> queryById(MongoDatabase db, String table, Object id) throws Exception {
        MongoCollection<Document> collection = db.getCollection(table);

        BasicDBObject query = new BasicDBObject("_id", id);

        FindIterable<Document> iterable = collection.find(query);

        /*for (Document document : iterable) {
            String zhou = document.getString("zhou");
            System.out.println(zhou);


        }*/

        Map<String, Object> jsonStrToMap = null;

        MongoCursor<Document> cursor = iterable.iterator();
        System.out.println(cursor);
        while (cursor.hasNext()) {
            Document user = cursor.next();
            String jsonString = user.toJson();
            jsonStrToMap = JsonStrToMap.jsonStrToMap(jsonString);


        }
        return jsonStrToMap;
    }

    public List<Map<String, Object>> queryByDoc(MongoDatabase db, String table, BasicDBObject doc) {

        //根据传入的db和table获取collection对象
        MongoCollection<Document> collection = db.getCollection(table);
        FindIterable<Document> iterable = collection.find(doc);
        /**
         * 获取迭代器，FindIterable<document/></document>
         * 获取游标MongoCursor<document></document>
         * 通过游标便利检索出文档嘉禾
         */
        List<Map<String, Object>> list = new ArrayList<>();

        MongoCursor<Document> cursor = iterable.iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            String jsonString = document.toJson();
            Map<String, Object> stringIntegerMap = JsonStrToMap.jsonStrToMap(jsonString);
            list.add(stringIntegerMap);

        }
        System.out.println("文档检索完毕");

        return list;
    }

    public List<Map<String, Object>> queryAll(MongoDatabase db, String table) {
        //获取数据库的collection
        MongoCollection<Document> collection = db.getCollection(table);

        //获取数据库的迭代器
        FindIterable<Document> iterable = collection.find();
        //获取数据库的游标
        MongoCursor<Document> cursor = iterable.iterator();

        List<Map<String, Object>> list = new ArrayList<>();

        while (cursor.hasNext()) {
            Document d = cursor.next();

            String toj = d.toJson();
            System.out.println(toj);

            Map<String, Object> stringIntegerMap = JsonStrToMap.jsonStrToMap(toj);

            list.add(stringIntegerMap);

        }
        return list;
    }

    public void printFindIterable(FindIterable<Document> iterable) {
        MongoCursor<Document> cursor = iterable.iterator();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            String toJson = document.toJson();
            System.out.println(toJson);

        }
        cursor.close();

    }

    /**
     * Insert Data
     *
     * @param db
     * @param table
     * @param document
     * @return
     */
    @Override
    public boolean insert(MongoDatabase db, String table, Document document) {
        MongoCollection<Document> collection = db.getCollection(table);
        collection.insertOne(document);
        long count = collection.count(document);

        System.out.println("count:" + count);

        if (count == 1) {
            System.out.println("文档插入成功");
            return true;
        } else {
            System.out.println("文档插入失败");
            return false;
        }
    }

    public boolean insertMany(MongoDatabase mongoDatabase, String table, List<Document> docs) {
        MongoCollection<Document> collection = mongoDatabase.getCollection(table);
        long preCount = collection.count();

        collection.insertMany(docs);
        long newCount = collection.count();

        long count = newCount - preCount;
        System.out.println("插入的数量为" + (newCount - preCount));
        if (count == docs.size()) {
            System.out.println("插入多个文档成功");
            return true;
        } else {
            System.out.println("插入多个文档失败");
            return false;
        }
    }
    /**
     * Delete Many Data.if doc is empty will delete all Data
     *
     * @param db
     * @param table
     * @param doc
     * @return
     */
    @Override
    public boolean delete(MongoDatabase db, String table, BasicDBObject doc) {
        MongoCollection<Document> collection = db.getCollection(table);
        DeleteResult deleteResult = collection.deleteMany(doc);

        long count = deleteResult.getDeletedCount();
        System.out.println("删除的数量为" + count);

        if (count > 0) {

            System.out.println("删除多个文档成功");
            return true;
        } else {
            System.out.println("删除失败");
            return false;
        }
    }

    public boolean deleteOne(MongoDatabase db, String table, BasicDBObject docment) {
        MongoCollection<Document> collection = db.getCollection(table);
        DeleteResult deleteResult = collection.deleteOne(docment);
        long deletedCount = deleteResult.getDeletedCount();
        System.out.println("删除的数量为" + deletedCount);
        if (deletedCount == 1) {
            System.out.println("删除一个成功");
            return true;
        } else {
            System.out.println("删除一个失败");
            return false;
        }
    }


    /**
     * update all data
     *
     * @param db
     * @param table
     * @param oldDoc
     * @param newDoc
     * @return
     */
    @Override
    public boolean update(MongoDatabase db, String table, BasicDBObject oldDoc, BasicDBObject newDoc) {
        MongoCollection<Document> collection = db.getCollection(table);
        UpdateResult result = collection.updateMany(oldDoc, new Document("$set", newDoc));
        long modifiedCount = result.getModifiedCount();
        System.out.println("更新的数量为：" + modifiedCount);

        if (modifiedCount > 0) {

            System.out.println("更新成功");
            return true;
        } else {
            System.out.println("更新失败");
            return false;
        }
    }

    public boolean updateOne(MongoDatabase db, String table, BasicDBObject old, BasicDBObject newDoc) {
        MongoCollection<Document> collection = db.getCollection(table);
        UpdateResult result = collection.updateOne(old, new Document("$set", newDoc));
        long modifiedCount = result.getModifiedCount();

        System.out.println("xiugai" + modifiedCount);

        if (modifiedCount == 1) {
            System.out.println("修改一个成功");
            return true;

        } else {
            System.out.println("修改一个失败");
            return false;

        }

    }

    public void createCol(MongoDatabase db, String table) {

        db.createCollection(table);
        System.out.println("创建集合成功");

    }

    public void deleteCol(MongoDatabase db, String table) {
        db.getCollection(table).drop();
        System.out.println("删除集合充公");

    }


}
