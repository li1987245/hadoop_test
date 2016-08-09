import org.junit.Test;
import util.HashMap;

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
    public void testHashMap(){
        HashMap<Integer,Object> map = new HashMap<>(2);
        Random r = new Random();
        map.put(15,1);
        map.put(7,1);
        for(int i=0;i<100;i++){
            int random = r.nextInt(30);
            map.put(random,1);
            System.out.println(random);
        }
        System.out.println(map.size());
        System.out.println(map.entrySet().size());

    }
}
