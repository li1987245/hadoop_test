import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import entity.Phone;
import entity.User;
import mybatis.ClassDao;
import mybatis.PhoneDao;
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
        sqlSessionFactory.getConfiguration().addMapper(ClassDao.class);
        sqlSessionFactory.getConfiguration().addMapper(PhoneDao.class);
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
    public void testSingleSelect() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            PhoneDao dao = session.getMapper(PhoneDao.class);
            Phone phone = session.selectOne("_selectPhone",1);
            System.out.println(phone.getUser().getName());
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
                    for (int k = 0; k < leaves.size(); k++){
                        JSONObject leaf = leaves.getJSONObject(k);
                        _leaves.add(leaf.getString("name"));
                    }
                    Sets.SetView view = Sets.intersection(ImmutableSet.of("百度"), _leaves);
                    sb.append(view.size() > 0);
                }
                System.out.println("id:");
                sb.append(id);
                sb.append("\tcondition:");
                sb.append(sb.toString());
            }

        } finally {
            session.close();
        }
    }

    @Test
    public void testInsertClass() {
        SqlSession session = sqlSessionFactory.openSession();
        try {
            ClassDao dao = session.getMapper(ClassDao.class);
            StringBuffer sb = new StringBuffer("");
            JSONArray arr = JSON.parseArray(sb.toString()
            );
            int _1 = 0;
            int _2 = 0;
            int _3 = 0;
            int _4 = 0;
            int _5 = 0;
            int _6 = 0;
            int _7 = 0;
            int _8 = 0;
            for (int i = 0; i < arr.size(); i++) {
                JSONObject json = arr.getJSONObject(i);
                Map<String, Object> map = new HashMap<>();
                if (json.containsKey("7")) {
                    map.put("id", json.getIntValue("开发调用序号"));
                    map.put("pid", _7);
                    classCommon(json, map);
                } else if (json.containsKey("6")) {
                    int id = json.getIntValue("开发调用序号");
                    _7 = id;
                    map.put("id", id);
                    map.put("pid", _6);
                    classCommon(json, map);
                } else if (json.containsKey("5")) {
                    int id = json.getIntValue("开发调用序号");
                    _6 = id;
                    map.put("id", id);
                    map.put("pid", _5);
                    classCommon(json, map);
                } else if (json.containsKey("4")) {
                    int id = json.getIntValue("开发调用序号");
                    _5 = id;
                    map.put("id", id);
                    map.put("pid", _4);
                    classCommon(json, map);
                } else if (json.containsKey("3")) {
                    int id = json.getIntValue("开发调用序号");
                    _4 = id;
                    map.put("id", id);
                    map.put("pid", _3);
                    classCommon(json, map);
                } else if (json.containsKey("2")) {
                    int id = json.getIntValue("开发调用序号");
                    _3 = id;
                    map.put("id", id);
                    map.put("pid", _2);
                    classCommon(json, map);
                } else if (json.containsKey("1")) {
                    int id = json.getIntValue("开发调用序号");
                    _2 = id;
                    map.put("id", id);
                    map.put("pid", _1);
                    classCommon(json, map);
                } else {
                    int id = json.getIntValue("开发调用序号");
                    _1 = id;
                    map.put("id", id);
                    map.put("pid", 0);
                    classCommon(json, map);
                }
                dao.insertClass(map);
                session.commit();
            }
        } finally {
            session.close();
        }
    }

    private void classCommon(JSONObject json, Map<String, Object> map) {
        if (json.containsKey("来源"))
            map.put("source", 1);
        else
            map.put("source", 0);
        if (json.containsKey("第三方信息"))
            map.put("third", 1);
        else
            map.put("third", 0);
        if (json.containsKey("主打产品"))
            map.put("main", 1);
        else
            map.put("main", 0);
        if (json.containsKey("热门主题"))
            map.put("hot", 1);
        else
            map.put("hot", 0);
        if (json.containsKey("行业筛选一级"))
            map.put("industry", 1);
        else if (json.containsKey("行业筛选二级"))
            map.put("industry", 2);
        else
            map.put("industry", 0);
        map.put("name", json.getString("中文名称"));
        
    }
}
