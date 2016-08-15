import demo.China;
import demo.Student;
import org.junit.Test;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;

/**
 * Created by jinwei.li@baifendian.com on 2016/5/5.
 */
public class BasicTest {
    @Test
    public void testFinally(){
        System.out.println(testFinally(1));
    }

    private int testFinally(int i) {
        try {
            System.out.println("try");
            throw new RuntimeException();
        }catch (Exception e){
            System.out.println("catch");
            return -1;
        }finally {
            System.out.println("finally");
            return -2;
        }
    }
    @Test
    public void testReflection(){
        Student student = new Student();
        Class cls = student.getClass();
        Type type = cls.getGenericSuperclass();
        System.out.println(type);
        ParameterizedType p=(ParameterizedType)type;
        Class c=(Class) p.getActualTypeArguments()[0];
        System.out.println(c);
    }


}
