package demo;

/**
 * Created by d on 2016/8/14.
 */
public class Person<T extends Country> {
    public void print(T t){
        t.say();
    }
}
