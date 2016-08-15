package util;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.HashMap;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Projections.*;

@SuppressWarnings("all")
public class MongodbUtil {

    //mongo 服务ip地址
    public static final String MONGODB_IP = PropertiesUtil.getStringValue("mongodb.ip");
    public static final int MONGO_PORT = PropertiesUtil.getIntegerValue("mongodb.port");
    //mongo 服务数据库
    public static final String MONGODB_DB = PropertiesUtil.getStringValue("database.info");
    //用户名
//    public static final String MONGODB_USER = PropertiesUtil.getStringValue("mongodb.user");
    //密码
//    public static final String MONGODB_PASSWORD = PropertiesUtil.getStringValue("mongodb.password");
    private volatile static MongoClient mongoClient;

    static {
        String MONGODB_PASSWORD_DECRYPT = null;
        ServerAddress serverAddress = new ServerAddress(MONGODB_IP, MONGO_PORT);
        List<ServerAddress> adders = new ArrayList<ServerAddress>();
        adders.add(serverAddress);
        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
//        MongoCredential credential = MongoCredential.createScramSha1Credential(MONGODB_USER, MONGODB_DB, MONGODB_PASSWORD_DECRYPT.toCharArray());
//        List<MongoCredential> credentials = new ArrayList<MongoCredential>();
//        credentials.add(credential);
        //通过连接认证获取MongoDB连接
//        mongoClient = new MongoClient(adders, credentials);
        mongoClient = new MongoClient(adders);
        MongoClientOptions.Builder options = new MongoClientOptions.Builder();
        options.connectionsPerHost(300);// 连接池设置为300个连接,默认为100
        options.connectTimeout(15000);// 连接超时，推荐>3000毫秒
        options.maxWaitTime(5000); //
        options.socketTimeout(0);// 套接字超时时间，0无限制
        options.threadsAllowedToBlockForConnectionMultiplier(5000);// 线程队列数，如果连接线程排满了队列就会抛出“Out of semaphores to get db”错误。
        options.build();
    }

    /**
     * 获取DB实例 - 指定DB
     *
     * @param dbName
     * @return
     */
    public static DB getOldDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            DB db = mongoClient.getDB(dbName);
            return db;
        }
        return null;
    }

    /**
     * 获取DB实例 - 指定DB
     *
     * @param dbName
     * @return
     */
    public static MongoDatabase getDB(String dbName) {
        if (dbName != null && !"".equals(dbName)) {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            return database;
        }
        return null;
    }

    /**
     * 获取collection对象 - 指定Collection
     *
     * @param collName
     * @return
     */
    public static MongoCollection<Document> getCollection(String dbName, String collName) {
        if (null == collName || "".equals(collName)) {
            return null;
        }
        if (null == dbName || "".equals(dbName)) {
            return null;
        }
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        return collection;
    }

    /**
     * 查询DB下的所有表名
     */
    public static List<String> getAllCollections(String dbName) {
        MongoIterable<String> colls = getDB(dbName).listCollectionNames();
        List<String> _list = new ArrayList<String>();
        for (String s : colls) {
            _list.add(s);
        }
        return _list;
    }

    /**
     * 获取所有数据库名称列表
     *
     * @return
     */
    public static MongoIterable<String> getAllDBNames() {
        MongoIterable<String> s = mongoClient.listDatabaseNames();
        return s;
    }

    /**
     * 删除一个数据库
     */
    public static void dropDB(String dbName) {
        getDB(dbName).drop();
    }

    /**
     * 查找对象 - 根据主键_id
     *
     * @param id
     * @return
     */
    public static Document findById(MongoCollection<Document> coll, String id) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Document myDoc = coll.find(eq("_id", _idobj)).first();
        return myDoc;
    }

    /**
     * 统计数
     */
    public static int getCount(MongoCollection<Document> coll) {
        int count = (int) coll.count();
        return count;
    }

    /**
     * 条件查询
     */
    public static MongoCursor<Document> find(MongoCollection<Document> coll, Bson filter) {
        return coll.find(filter).iterator();
    }

    /**
     * 分页查询
     */
    public static MongoCursor<Document> findByPage(MongoCollection<Document> coll, Bson filter, int pageNo, int pageSize) {
        Bson orderBy = new BasicDBObject("_id", 1);
        return coll.find(filter).sort(orderBy).skip((pageNo - 1) * pageSize).limit(pageSize).iterator();
    }

    /**
     * 通过ID删除
     *
     * @param coll
     * @param id
     * @return
     */
    public static int deleteById(MongoCollection<Document> coll, String id) {
        int count = 0;
        ObjectId _id = null;
        try {
            _id = new ObjectId(id);
        } catch (Exception e) {
            return 0;
        }
        Bson filter = eq("_id", _id);
        DeleteResult deleteResult = coll.deleteOne(filter);
        count = (int) deleteResult.getDeletedCount();
        return count;
    }

    /**
     * 根据过滤条件从指定缓存对象中获取对象个数
     * getCount:
     *
     * @param databasename
     * @param collectionname
     * @param map
     * @return
     */
    public static long getCount(String databasename, String collectionname, HashMap<String, Object> map) {
        BasicDBObject basic = makeCondition(map);
        try {
            MongoCollection col = getCollection(databasename, collectionname);
            long find = col.count(basic);
            return find;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 更新
     *
     * @param databasename
     * @param collectionname
     * @param condition
     * @param value
     * @return
     */
    public static boolean update(String databasename, String collectionname, HashMap<String, Object> condition, HashMap<String, Object> value) {
        boolean result = false;
        MongoCollection col = getCollection(databasename, collectionname);
        BasicDBObject _condition = new BasicDBObject(condition);
        BasicDBObject _value = new BasicDBObject(value);
        UpdateResult writeResult = col.updateOne(_condition, new Document("$set", _value));
        result = writeResult.isModifiedCountAvailable();
        return result;
    }

    /**
     * 更新
     *
     * @param databasename
     * @param collectionname
     * @param condition
     * @param value
     * @return
     */
    public static boolean update(String databasename, String collectionname, Map<String, Object> condition, Object value) {
        boolean result = false;
        MongoCollection col = getCollection(databasename, collectionname);
        BasicDBObject _condition = new BasicDBObject(condition);
        BasicDBObject _value = new BasicDBObject(BeanUtil.bean2DBObj(value));
        UpdateResult writeResult = col.updateOne(_condition, new Document("$set", _value));
        result = writeResult.isModifiedCountAvailable();
        return result;
    }

    /**
     * getByCondition:(根据过滤条件从指定缓存对象中获取对象).
     *
     * @param databasename
     * @param collectionname
     * @param map
     * @param clazz
     * @return
     */
    public static <T> List<T> getByCondition(String databasename, String collectionname,
                                             Object map, Class<T> clazz, String... fields) {
        BasicDBObject basic = makeCondition(map);
        BasicDBObject projection = makeProjection(fields);
        return getList(databasename, collectionname, clazz, basic, projection);
    }

    /**
     * getList:(根据条件过滤集合).
     *
     * @param databasename
     * @param collectionname
     * @param clazz
     * @param basic
     * @return
     */
    private static <T> List<T> getList(String databasename, String collectionname,
                                       Class<T> clazz, BasicDBObject basic, BasicDBObject projection) {
        List<T> list = new ArrayList<T>();
        try {
            MongoCollection col = getCollection(databasename, collectionname);
            MongoCursor<Document> find = col.find(basic).projection(projection).iterator();
            while (find.hasNext()) {
                Document dbObject = find.next();
                T b = clazz.newInstance();
                b = BeanUtil.dbObj2Bean(dbObject, b);
                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 组装查询条件
     * getByCondition:
     *
     * @return
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static Object makeCondition(String key, Object obj) {
        BasicDBObject basic = new BasicDBObject();

        if (obj != null) {
            if (obj instanceof HashMap<?, ?>) {
                HashMap<String, Object> map = (HashMap) obj;
                Set<String> keys = map.keySet();
                basic = new BasicDBObject();
                for (String k : keys) {
                    Object basicObject = makeCondition(k, map.get(k));
                    basic.append(k, basicObject);
                }
            } else {
                return obj;
            }
        }
        return basic;
    }

    /**
     * 组装查询条件
     * getByCondition:
     *
     * @param map
     * @return
     */
    public static BasicDBObject makeCondition(Object obj) {
        return (BasicDBObject) makeCondition(null, obj);
    }

    /**
     * 组装查询条件
     * getByCondition:
     *
     * @param map
     * @return
     */
    public static BasicDBObject makeProjection(String... fields) {
        BasicDBObject basic = new BasicDBObject();

        if (fields != null) {
            for (String field : fields) {
                basic.append(field, 1);
            }
        }
        return basic;
    }

    /**
     * @param coll
     * @param id
     * @param newdoc
     * @return
     */
    public static Document updateById(MongoCollection<Document> coll, String id, Document newdoc) {
        ObjectId _idobj = null;
        try {
            _idobj = new ObjectId(id);
        } catch (Exception e) {
            return null;
        }
        Bson filter = eq("_id", _idobj);
        // coll.replaceOne(filter, newdoc); // 完全替代
        coll.updateOne(filter, new Document("$set", newdoc));
        return newdoc;
    }

    public static void dropCollection(String dbName, String collName) {
        getDB(dbName).getCollection(collName).drop();
    }

    /**
     * 关闭Mongodb
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }

    /**
     * 插入
     *
     * @param dbName
     * @param collName
     * @param obj
     */
    public static void insert(String dbName, String collName, Object obj) {
        MongoCollection<Document> collection = mongoClient.getDatabase(dbName).getCollection(collName);
        Document doc = new Document(BeanUtil.bean2DBObj(obj));
        collection.insertOne(doc);
    }

    public static void main(String[] args) {
//        MongoCollection<Document> collection = MongodbUtil.getCollection(MONGODB_DB,"user");
//        collection.find(and(eq("", 20))).projection(include("quantity"));
//        String[] names = {"tom","jack","jetty","lucy"};
//        String[] addrs = {"北京","天津","上海","河北","河南","山西","湖南","广东","香港"};
//        MongoDatabase database = MongodbUtil.getDB("test");
//        database.createCollection("user");
//        MongoCollection<Document> collection = MongodbUtil.getCollection("test","user");
//        List<Document> list = null;
//        for(long i = 0;i<100000000;i++){
//            if(i%10000 == 0){
//                if(list!=null)
//                    collection.insertMany(list);
//                list = new ArrayList<>();
//            }
//            Document doc = new Document("name",names[(int) (i%4)]).append("age",i%80).append("address",addrs[(int) (i%9)]).append("time",System.currentTimeMillis());
//            list.add(doc);
//        }
//        collection.insertMany(list);
        GridFS fs = new GridFS(MongodbUtil.getOldDB("test"));
        try {
//            GridFSInputFile inputFile =  fs.createFile(new File("C:\\Users\\d\\Desktop\\百分点工时系统使用手册V1.0.pdf"));
//            System.out.println(inputFile.getId());
//            inputFile.save();
            GridFSDBFile file =  fs.findOne(new ObjectId("57af3e7617251328e883d93d"));
            file.writeTo(new File("C:\\\\Users\\\\d\\\\Desktop\\\\百分点工时系统使用手册V2.0.pdf"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        MongodbUtil.close();
    }
}
