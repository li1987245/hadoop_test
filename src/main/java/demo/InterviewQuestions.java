package demo;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.MapDifference;
import com.google.common.collect.Maps;

import java.nio.ByteBuffer;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.base.Preconditions.*;

/**
 * Created by d on 2016/12/8.
 */
public class InterviewQuestions {
    public static void main(String[] args) throws Exception {
//        Integer i0 = 10;
//        Integer i1 = 10;
//        System.out.println(i0==i1);
//        String str0 = "aaa";
//        String str1 = "aaa";
//        System.out.println(str0==str1);
//        ThreadLocal local = new ThreadLocal();
//        local.remove();
//        Long.toBinaryString(100);
//        DecimalFormat df = new DecimalFormat("#.##");
//        System.out.println(Math.round(Double.MAX_VALUE/Long.MAX_VALUE));
//        System.out.println(df.format(Double.MAX_VALUE/Long.MAX_VALUE));
//        System.out.println(Long.MIN_VALUE);
//        System.out.println(Long.toBinaryString(Long.MIN_VALUE));
//        System.out.println(Long.toBinaryString(Long.MIN_VALUE).length());
//        System.out.println(Long.toBinaryString(Double.doubleToLongBits(Double.MAX_VALUE)));
//        TreeMap<String,String> map = new TreeMap<>();
//        map.containsKey("");
//        map.put("1","a");
//        map.put("6","d");
//        map.put("5","c");
//        map.put("3","b");
//        map.put("6","e");
//        map.keySet().forEach(x-> System.out.println(x));
//        System.out.println(Runtime.getRuntime().maxMemory()/1024/1024/1024);
        List<String> lst = new ArrayList();
        lst.add("1");
        lst.add("2");
        lst.add("3");
        lst.add("4");
        lst.add("2");
        lst.add("3");
        lst.add("4");
        lst.add("2");
        lst.add("3");
        lst.add("2");
//        for(int i=lst.size()-1;i>=0;i--){
//            System.out.println(lst.get(i));
//            if(i==1)
//                lst.remove(i);
//        }
//        Iterator iterator = lst.iterator();
//        while (iterator.hasNext()){
//            if(iterator.next().equals("2"))
//                iterator.remove();
//        }
//        System.out.println(Arrays.deepToString(lst.toArray()));
//        ByteBuffer bf = ByteBuffer.allocate(128);
//        new StringBuffer().reverse();
//        lst.parallelStream()
        // 1. Individual values
//        Stream stream = Stream.of("a", "b", "c");
        // 2. Arrays
//        String [] strArray = new String[] {"a", "b", "c"};
//        stream = Stream.of(strArray);
//        stream = Arrays.stream(strArray);
        // 3. Collections
//        List<String> list = Arrays.asList(strArray);
//        stream = list.stream();
        // 1. Array
//        String[] strArray1 = stream.toArray(String[]::new);
        // 2. Collection
//        List<String> list1 = (List<String>) stream.collect(Collectors.toList());
        // 3. String
//        String str = stream.collect(Collectors.joining(",")).toString();
//        System.out.println(str);
//        一对多
//        Stream<List<Integer>> inputStream = Stream.of(
//                Arrays.asList(1),
//                Arrays.asList(2, 3),
//                Arrays.asList(4, 5, 6)
//        );
//        Stream<Integer> outputStream = inputStream.
//                flatMap((childList) -> childList.stream());
//        List<Integer> nums = Arrays.asList(1, 2, 3, 4);
//        List<Integer> squareNums = nums.stream().
//                map(n -> n * n).
//                collect(Collectors.toList());
//        int count = lst.parallelStream().map(x -> Integer.parseInt(x)).filter(x -> x > 0).reduce(0,Integer::sum);
//        System.out.println(count);
//        Map<String,String> map = new HashMap(){
//            {
//                put("1","a");
//            }
//        };
//        Collections.unmodifiableMap(map);
//        ImmutableMap<String,String> _map = ImmutableMap.of("1","a","2","b");
//        checkNotNull(lst);
//        Map<String, Object> mapA = ImmutableMap.of("key1", 1,"key11", 11, "key2", 3);
//        Map<String, Object> mapB = ImmutableMap.of("key11", 11, "key2", 2);
//        MapDifference differenceMap = Maps.difference(mapA, mapB);
//        differenceMap.areEqual();
//        Map entriesDiffering = differenceMap.entriesDiffering();
//        entriesDiffering.forEach((x, y) -> {
//            System.out.println("entriesDiffering\t"+x + "\t" + y);
//        });
//        Map entriesOnlyOnLeft = differenceMap.entriesOnlyOnLeft();
//        entriesOnlyOnLeft.forEach((x, y) -> {
//            System.out.println("entriesOnlyOnLeft\t"+x + "\t" + y);
//        });
//        Map entriesOnlyOnRight = differenceMap.entriesOnlyOnRight();
//        entriesOnlyOnRight.forEach((x, y) -> {
//            System.out.println("entriesOnlyOnRight\t"+x + "\t" + y);
//        });
//        Map entriesInCommon = differenceMap.entriesInCommon();
//        entriesInCommon.forEach((x, y) -> {
//            System.out.println("entriesInCommon\t"+x + "\t" + y);
//        });
//        Map<String,Integer> map = new HashMap<>();
//        map.put("a",1);
//        map.put("b",3);
//        map.put("e",5);
//        map.put("c",2);
//        List<Map.Entry<String,Integer>> list = new LinkedList<>(map.entrySet());
//        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
//            @Override
//            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
//                return o1.getValue().compareTo(o2.getValue());
//            }
//        });
//        list.forEach(x->{
//            System.out.println(x.getKey()+x.getValue());
//        });
//        List list1 = Lists.newArrayList(3, 1, 2);
//        Collections.sort(list1, new Comparator<Integer>() {
//            @Override
//            public int compare(Integer o1, Integer o2) {
//                return o1-o2;
//            }
//        });
//        list1.forEach(x-> System.out.println(x));
    }
}
