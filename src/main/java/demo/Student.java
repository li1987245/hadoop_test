package demo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by d on 2016/8/14.
 */
public class Student extends Person<China> {

    public static void main(String[] args) {
        Student student = new Student();
        student.print(new China());
        Class cls = student.getClass();
        Type type = cls.getGenericSuperclass();
        System.out.println(type);
        ParameterizedType p=(ParameterizedType)type;
        Class c=(Class) p.getActualTypeArguments()[0];
        System.out.println(c);
    }
}
