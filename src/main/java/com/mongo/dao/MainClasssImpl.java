package com.mongo.dao;

import com.mongo.MongoHelper;
import com.mongo.dao.impl.MongoDaoImpl;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sun.xml.internal.fastinfoset.alphabet.BuiltInRestrictedAlphabets.table;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 15:37
 */
public class MainClasssImpl {
    public static String table = "aaPoint";
    public static void main(String[] args) {
        MongoClient mongoClient = MongoHelper.getMongoClient();
        MongoDatabase mongoDataBase = MongoHelper.getMongoDataBase(mongoClient);
        MongoDaoImpl mongoDaoImpl = new MongoDaoImpl();

        //language=JSON
        String jsonStr = "{\n" +
                "  \"_id\": 1,\n" +
                "  \"number\": \"AA001\",\n" +
                "  \"name\": \"角色AA签名\",\n" +
                "  \"currentUsedVersion\": \"v2\",\n" +
                "  \"associationVersion\": \"1\"\n" +
                "}";

        Object parse = JSON.parse(jsonStr);
        Map<String, Object> map = (Map<String, Object>) parse;

        Document document = new Document(map);
        mongoDaoImpl.insert(mongoDataBase, table, document);


       /* Map<String, Object> map = new HashMap<>();
        map.put("shanghai", 1);
        map.put("tianjian", 2);
//        mongoDaoImpl.insert(mongoDataBase, table, new Document(map));//插入document
        List<Map<String, Integer>> list = mongoDaoImpl.queryByDoc(mongoDataBase, table, new BasicDBObject(map));
        for (Map<String, Integer> stringIntegerMap : list) {
            for (Map.Entry<String, Integer> stringIntegerEntry : stringIntegerMap.entrySet()) {
                System.out.println(stringIntegerEntry.getKey() + "------>" + stringIntegerEntry.getValue());

            }
        }*/

/*        mongoDaoImpl.createCol(mongoDataBase, "aaPoint");
        mongoDaoImpl.createCol(mongoDataBase, "aaPointVersion");
        mongoDaoImpl.createCol(mongoDataBase, "signatureItem");
        mongoDaoImpl.createCol(mongoDataBase, "verificationPoint");
        mongoDaoImpl.createCol(mongoDataBase, "verificationPointVersion");*/

//        try {
//            mongoDaoImpl.queryById(mongoDataBase, table, 1);//检索event_id,注意id类型是字符串还是int
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        BasicDBObject document2 = new BasicDBObject("likes", 1000);
//        document2.append("event_id", "55");
//        mongoDaoImpl.queryByDoc(mongoDataBase, table, document2);//检索doc,可以根据doc(key,value)来查找,当doc是空的时候，检索全部
//        mongoDaoImpl.queryAll(mongoDataBase, table); //查询全部
//
//        BasicDBObject document3 = new BasicDBObject("likes", 200);
//        mongoDaoImpl.delete(mongoDataBase, table, document3);//删除doc 的全部信息，当doc 是空，则删除全部
//        BasicDBObject document4 = new BasicDBObject("likes", 1000);
//        mongoDaoImpl.deleteOne(mongoDataBase, table, document4);//删除doc 的一个信息
//
////        更新文档 将文档中likes = 100 的文档修改为likes = 200
//        BasicDBObject whereDoc = new BasicDBObject("likes", 1000);
//        BasicDBObject updateDoc = new BasicDBObject("likes", 255);
//        mongoDaoImpl.update(mongoDataBase, table, whereDoc, updateDoc);//更新全部,查找到oldDoc的数据，更新newDoc的数据
//        BasicDBObject whereDoc2 = new BasicDBObject("likes", 255);
//        BasicDBObject updateDoc2 = new BasicDBObject("event_id", 205);
//        mongoDaoImpl.updateOne(mongoDataBase, table, whereDoc2, updateDoc2);//更新全部,查找到oldDoc的数据，更新newDoc的数据
    }
}
