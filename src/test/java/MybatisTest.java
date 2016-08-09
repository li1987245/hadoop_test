import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import entity.User;
import mybatis.UserDao;
import mybatis.UserGroupDao;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/5.
 */
public class MybatisTest {
    SqlSessionFactory sqlSessionFactory;

    @Before
    public void setUp() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSessionFactory.getConfiguration().addMapper(UserDao.class);
        sqlSessionFactory.getConfiguration().addMapper(UserGroupDao.class);
    }

    @Test
    public void testInsert() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            UserDao dao = session.getMapper(UserDao.class);
//            User user1 = new User();
//            user1.setName("tom");
//            User user2 = new User();
//            user2.setName("jack");
//            List<User> list = new ArrayList<User>();
//            list.add(user1);
//            list.add(user2);
//            session.insert("bulkInsertUser",list);
//            session.commit();
            User user = dao.selectUser(3);
            List<Object> _user = session.selectList("selectUserAndPhone", 3);
            for (Object u : _user) {
                System.out.println(JSON.toJSONString(u));
            }
        } finally {
            session.close();
        }
    }

    @Test
    public void testSelect() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            String hbaseName = "V3";
            String family = "family";
            UserGroupDao dao = session.getMapper(UserGroupDao.class);
            List<Map<String, String>> list = dao.getLableConfig();
            Map<String, String> labelConfig = new HashMap<String, String>();
            for (Map<String, String> m : list) {
                String key = m.get("third_level_id");
                String value = m.get("hbase_column");
                labelConfig.put(key, value);
            }
            JSONArray userGroups;
            userGroups = JSONArray.parseArray(JSON.toJSONString(dao.getUserGroupCondition()));
            for (int i = 0; i < userGroups.size(); i++) {
                StringBuilder sb = new StringBuilder();
                JSONObject json = userGroups.getJSONObject(i);
                String id = json.getString("id");
                String condition = json.getString("group_condition");
                JSONArray conArr = JSONArray.parseArray(condition);
                for (int j = 0; j < conArr.size(); j++) {
                    JSONObject con = conArr.getJSONObject(j);
                    //第一次不进行判断
                    if (j > 0) {
                        int symbol = con.getIntValue("symbol");
                        //如果为0，表示and关系
                        if (symbol == 0)
                            sb.append("&&");
                        else
                            sb.append("||");
                    }
                    String labelId = con.getString("labelId");
                    JSONArray leaves = con.getJSONArray("leafs");
                    Set<String> _leaves = new HashSet<String>();
                    for(int k=0;k<leaves.size();k++){
                        JSONObject leaf = leaves.getJSONObject(k);
                        _leaves.add(leaf.getString("name"));
                    }
                    Sets.SetView view = Sets.intersection(ImmutableSet.of("百度"), _leaves);
                    sb.append(view.size() > 0);
                }
                System.out.println("id:"+id+"\tcondition:"+sb.toString());
            }

        } finally {
            session.close();
        }
    }
}
