package com.mall.common;

import com.mall.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-19
 * @Time: 18:15
 */
public class RedisPool {
    /**
     * jedis连接池
     */
    private static JedisPool pool;

    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total", "20"));
    /**
     * 在jedispool中最小的idle状态(空闲)的jedis实例的个数
     */
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle", "20"));
    /** 在jedispool中最大的idle状态(空闲)的jedis实例的个数*/

    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle", "2"));

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow", "true"));

    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return", "false"));

    private static String redisIp = PropertiesUtil.getProperty("redis1.ip", "127.0.0.1");
    private static Integer redisPort = Integer.parseInt(PropertiesUtil.getProperty("redis1.port", "6379"));


    private static void initPool() {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(maxIdle);
        config.setMaxTotal(maxTotal);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);
        //最大连接耗尽是否阻塞，false会抛出异常，true组设知道超时，默认为true

        config.setBlockWhenExhausted(true);

        pool = new JedisPool(config, redisIp, redisPort);

    }

    static {
        initPool();

    }

    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void returnBrokenResource(Jedis jedis) {
        pool.returnBrokenResource(jedis);


    }

    public static void returnResource(Jedis jedis) {
        pool.returnResource(jedis);

    }

    public static void main(String[] args) {
        Jedis jedis = pool.getResource();
        jedis.set("zhouhengKey", "zhouhengValue");
        pool.destroy();
        System.out.println("ll");

    }



}
