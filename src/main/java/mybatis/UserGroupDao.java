package mybatis;

import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/5.
 */
public interface UserGroupDao {
    @Select("select id,group_condition from customer_group")
    public List<Map<String,String>> getUserGroupCondition();
    @Select("select third_level_id,hbase_table,hbase_column from label_config")
    public List<Map<String,String>> getLableConfig();
}
