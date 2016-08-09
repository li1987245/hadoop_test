package hadoop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.sun.jndi.toolkit.url.Uri;
import mybatis.UserGroupDao;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.*;


/**
 * Created by jinwei.li@baifendian.com on 2016/5/4.
 */
public class Test extends Configured implements Tool {
    SqlSessionFactory sqlSessionFactory;
    @Override
    public int run(String[] args) throws Exception {
        Configuration conf = getConf();
        conf.set("hbase.zookeeper.quorum", "172.19.1.166,172.19.1.167,172.19.1.168");
        conf.set("zookeeper.znode.parent", "/hbase");
        conf.set("hbase_table", "OfflineUserProfileV3");
        conf.set("hbase_family", "up");
        //mybatis
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSessionFactory.getConfiguration().addMapper(UserGroupDao.class);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserGroupDao dao = session.getMapper(UserGroupDao.class);
            conf.set("user_group",JSON.toJSONString(dao.getUserGroupCondition()));
            List<Map<String,String>> list = dao.getLableConfig();
            Map<String,String> labelConfig = new HashMap<String, String>();
            for(Map<String,String> m:list){
                String key = m.get("third_level_id");
                String value = m.get("hbase_column");
                labelConfig.put(key,value);
            }
            conf.set("label_config",JSON.toJSONString(labelConfig));
        } finally {
            session.close();
        }
        Job job = new Job(getConf());

        job.setJobName("test");

        job.setJarByClass(Test.class);
        Scan scan = new Scan();
        scan.setCaching(500);        // 1 is the default in Scan, which will be bad for MapReduce jobs
        scan.setCacheBlocks(false);  // don't set to true for MR jobs
        // set other scan attrs
        SingleColumnValueFilter filter = new SingleColumnValueFilter(
                Bytes.toBytes("up"),
                Bytes.toBytes("population_attribute:area:sex"),
                CompareFilter.CompareOp.NOT_EQUAL,
                Bytes.toBytes("0"));
        filter.setFilterIfMissing(true);
        scan.setFilter(filter);

        TableMapReduceUtil.initTableMapperJob(
                conf.get("hbase_table"),      // input table
                scan,             // Scan instance to control CF and attribute selection
                TestMapper.class,   // mapper class
                Text.class, // mapper output key
                Text.class, // mapper output value
                job);
        FileSystem.get(URI.create("hdfs://172.19.1.166:9000"),conf).delete(new Path("/test/oppo/"), true);
        FileOutputFormat.setOutputPath(job, new Path("hdfs://172.19.1.166:9000/test/oppo/"));
//        job.setReducerClass(TestReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setNumReduceTasks(0);

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitcode = ToolRunner.run(new Test(), args);
        System.exit(exitcode);
    }

    private static class TestMapper extends TableMapper<Text, Text> {

        private HTable userProfile;
        private String family;
        private JSONArray userGroups;
        private Map<String,String> labelConfig;
        private ScriptEngine engine;

        @Override
        protected void setup(Context context) throws IOException,
                InterruptedException {
            Configuration config = context.getConfiguration();
            String hbase_table = config.get("hbase_table");
            family=config.get("hbase_family");
            userProfile = new HTable(config, hbase_table);
            userGroups = JSONArray.parseArray(config.get("user_group"));
            labelConfig = JSON.parseObject(config.get("label_config"),Map.class);
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("js");
        }

        @Override
        protected void map(ImmutableBytesWritable key, Result value,
                           Context context) throws IOException, InterruptedException {
            String _key = new String(key.copyBytes());
            for(int i=0;i<userGroups.size();i++){
                JSONObject json = userGroups.getJSONObject(i);
                String id=json.getString("id");
                String condition=json.getString("group_condition");
                JSONArray conArr = JSONArray.parseArray(condition);
                StringBuilder sb = new StringBuilder();
                for(int j=0;j<conArr.size();j++){
                    JSONObject con = conArr.getJSONObject(j);
                    //第一次不进行判断
                    if(j>0){
                        int symbol = con.getIntValue("symbol");
                        //如果为0，表示and关系
                        if(symbol==0)
                            sb.append("&&");
                        else
                            sb.append("||");
                    }
                    String labelId=con.getString("labelId");
                    JSONArray leaves = con.getJSONArray("leafs");
                    Set<String> _leaves = new HashSet<String>();
                    //遍历得到叶子节点
                    for(int k=0;k<leaves.size();k++){
                        JSONObject leaf = leaves.getJSONObject(k);
                        _leaves.add(leaf.getString("name"));
                    }
                    String column = labelConfig.get(labelId);
                    byte[] bytes = value.getValue(family.getBytes(), column.getBytes());
                    if(bytes==null||bytes.length==0) {
                        sb.append("false");
                        continue;
                    }
                    JSONObject columnValue = JSONObject.parseObject(new String(bytes));
                    JSONArray values = columnValue.getJSONArray(column);
                    Set<String> _values = new HashSet<String>();
                    for(int k=0;k<values.size();k++){
                        JSONObject v = values.getJSONObject(k);
                        _values.add(v.getString("value"));
                        System.out.println(v.getString("value"));
                    }
                    Sets.SetView view = Sets.intersection(_values, _leaves);
                    sb.append(view.size() > 0);
                }
                try {
                    //如果符合条件，输出
                    if((Boolean)engine.eval(sb.toString())){
                        System.out.println("rowkey:"+_key+"\tid:"+id);
                        context.write(new Text(id),new Text(_key));
                    }
                    else{
                        System.out.println("error-----------rowkey:"+_key+"\tcondition:"+condition);
                    }
                } catch (ScriptException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class TestReducer extends Reducer<Text, IntWritable, Text, IntWritable> {


        public void setup(Context context) {
            // create DB connection...
        }

        public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            // do summarization
            // in this example the keys are Text, but this is just an example
        }

        public void cleanup(Context context) {
            // close db connection
        }

    }
}
