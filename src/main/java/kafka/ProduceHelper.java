package kafka;


import com.alibaba.fastjson.JSON;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import kafka.serializer.StringEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by d on 2016/8/10.
 */
public class ProduceHelper {
    private static Producer<String, String> producer = null;

    static {
        ProducerConfig config = config();
        producer = new Producer<String, String>(config);
    }

    private static ProducerConfig config() {

        Properties props = new Properties();
        props.put("zookeeper.connect", "hlg-3p109-lijinwei:2181,hlg-3p110-lijinwei:2181,hlg-3p111-lijinwei:2181/kafka");//声明zk
        props.put("metadata.broker.list", "hlg-3p112-lijinwei:9092");
        props.put("serializer.class", StringEncoder.class.getName());
        props.put("partitioner.class", "kafka.SimplePartitioner");
        props.put("request.required.acks", "1");
        ProducerConfig config = new ProducerConfig(props);
        return config;
    }

    public static void send(String topic, String pKey, String value) {
        KeyedMessage<String, String> data = new KeyedMessage<String, String>(topic, pKey, value);
        producer.send(data);
    }

    public static void close() {
        if (producer != null) {
            producer.close();
        }
    }

    public static void main(String[] args) {
        for(int i=0;i<1000;i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("int", i);
            ProduceHelper.send("test", Integer.toString(i), JSON.toJSONString(map));
        }
        ProduceHelper.close();
    }
}
