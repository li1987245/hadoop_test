package mybatis;

import org.apache.ibatis.annotations.Insert;

import java.util.Map;

/**
 * Created by d on 2016/8/20.
 */
public interface ClassDao {
    @Insert("insert into t_class values(#{id},#{pid},#{name},#{source},#{third},#{main},#{hot},#{industry})")
    public void insertClass(Map<String,Object> map);
}
