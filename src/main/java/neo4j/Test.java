package neo4j;

import com.alibaba.fastjson.JSON;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/4.
 */
public class Test {
    private static final String DB_PATH = "music";
    private static GraphDatabaseService db;

    private static enum RelationshipTypes implements RelationshipType {
        PUBLISH, CONTAIN
    }

    public static void useNodeAndRelationship() {
        Transaction tx = db.beginTx();
        try {
            Node node1 = db.createNode();
            node1.setProperty("name", "歌手 1");
            Node node2 = db.createNode();
            node2.setProperty("name", "专辑 1");
            node1.createRelationshipTo(node2, RelationshipTypes.PUBLISH);
            Node node3 = db.createNode();
            node3.setProperty("name", "歌曲 1");
            node2.createRelationshipTo(node1, RelationshipTypes.PUBLISH);
            node2.createRelationshipTo(node3, RelationshipTypes.CONTAIN);
            tx.success();
        } finally {
            tx.close();
        }
    }

    public static void getNodeAndRelationship() {
        String query = "MATCH (n) RETURN n.name";
        Result result = db.execute(query);
        while (result.hasNext()) {
            System.out.println(JSON.toJSONString(result.next()));
        }
    }

    public static void main(String[] args) {
        db = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder(DB_PATH).newGraphDatabase();
        Test.useNodeAndRelationship();
        Test.getNodeAndRelationship();
        db.shutdown();
    }
}
