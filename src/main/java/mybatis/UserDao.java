package mybatis;

import entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/5.
 */
public interface UserDao {
    @Select("SELECT * FROM t_user WHERE id = #{id}")
    User selectUser(int id);

    void bulkInsertUser(List<User> list);
}
