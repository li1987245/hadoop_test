import org.junit.*;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import retrofit.RestAdapter;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/18.
 */
public class Neo4jTest {
    private static final String API_URL = "http://172.24.3.142:7474/db/data/";
    private static final String DB_PATH = "";

    @Before
    public void setup() {
    }

    @Test
    public void useNodeAndRelationship() {
        GraphDatabaseService db = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        Transaction tx = db.beginTx();
        try {
            Node node1 = db.createNode();
            node1.setProperty("name", "歌手 1");
            Node node2 = db.createNode();
            node2.setProperty("name", "专辑 1");
            node1.createRelationshipTo(node2, RelationshipTypes.PUBLISH);
            Node node3 = db.createNode();
            node3.setProperty("name", "歌曲 1");
            node2.createRelationshipTo(node3, RelationshipTypes.CONTAIN);
            tx.success();
        } finally {
            tx.success();
        }
    }

    @After
    public  void shutdown() {
    }

    private static enum RelationshipTypes implements RelationshipType {
        PUBLISH, CONTAIN
    }
}
