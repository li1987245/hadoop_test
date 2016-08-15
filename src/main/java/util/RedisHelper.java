package util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis 工具类
 * Created by jinwei.li on 2016/7/9.
 */
@SuppressWarnings("all")
public class RedisHelper {
    private static String ADDR = PropertiesUtil.getStringValue("redis.ip");

    //Redis的端口号
    private static int PORT = PropertiesUtil.getIntegerValue("redis.port");

    //访问密码
    private static String AUTH = PropertiesUtil.getStringValue("redis.password");

    //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
    private static int MAX_ACTIVE = 1024;

    //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
    private static int MAX_IDLE = 200;

    //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
    private static int MAX_WAIT = 10000;

    //超时时间
    private static int TIMEOUT = 10000;

    //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
    private static boolean TEST_ON_BORROW = true;

    //链接池名
    private static JedisPool jedisPool = null;

    /**
     * 初始化Redis连接池
     */
    static {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
//            jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, DESUtils.decrypt(AUTH));
            jedisPool = new JedisPool(config,ADDR,PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取Jedis实例
     *
     * @return
     */
    public synchronized static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jedis;
    }

    /**
     * 插入数据
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = getJedis();
        String oldValue = jedis.set(key, value);
        release(jedis);
        return oldValue;
    }

    /**
     * 插入有缓存时间数据
     *
     * @param key
     * @param value
     * @param second
     * @return
     */
    public static String setEx(String key, String value, int second) {
        Jedis jedis = getJedis();
        String oldValue = jedis.setex(key, second, value);
        release(jedis);
        return oldValue;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = getJedis();
        String value = jedis.get(key);
        release(jedis);
        return value;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public static boolean isExist(String key) {
        Jedis jedis = getJedis();
        boolean value = jedis.exists(key);
        release(jedis);
        return value;
    }

    /**
     * 释放Jedis资源
     *
     * @param jedis
     */
    public static void release(final Jedis jedis) {
        if (jedis == null) {
            return;
        }
        jedis.close();
    }

    public static void main(String[] args) {
        RedisHelper.set("name", "tom");
    }
}
