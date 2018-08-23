package com.mall.common;


import com.mall.util.PropertiesUtil;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geely
 */
public class RedisShardedPool {
    private static ShardedJedisPool pool;//sharded jedis连接池
    private static Integer maxTotal = Integer.parseInt(PropertiesUtil.getProperty("redis.max.total","20")); //最大连接数
    private static Integer maxIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.max.idle","20"));//在jedispool中最大的idle状态(空闲的)的jedis实例的个数
    private static Integer minIdle = Integer.parseInt(PropertiesUtil.getProperty("redis.min.idle","20"));//在jedispool中最小的idle状态(空闲的)的jedis实例的个数

    private static Boolean testOnBorrow = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow","true"));//在borrow一个jedis实例的时候，是否要进行验证操作，如果赋值true。则得到的jedis实例肯定是可以用的。
    private static Boolean testOnReturn = Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return","true"));//在return一个jedis实例的时候，是否要进行验证操作，如果赋值true。则放回jedispool的jedis实例肯定是可以用的。

    private static String redisIp6380 = PropertiesUtil.getProperty("redis.6380.ip", "127.0.0.1");
    private static Integer redisPort6380 = Integer.parseInt(PropertiesUtil.getProperty("redis.6380.port", "6380"));

    private static String redisIp6381 = PropertiesUtil.getProperty("redis.6381.ip", "127.0.0.1");
    private static Integer redisPort6381 = Integer.parseInt(PropertiesUtil.getProperty("redis.6381.port", "6381"));

    private static String redisIp6382 = PropertiesUtil.getProperty("redis.6382.ip", "127.0.0.1");
    private static Integer redisPort6382 = Integer.parseInt(PropertiesUtil.getProperty("redis.6382.port", "6382"));


    private static void initPool(){
        JedisPoolConfig config = new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);

        config.setTestOnBorrow(testOnBorrow);
        config.setTestOnReturn(testOnReturn);

        config.setBlockWhenExhausted(true);//连接耗尽的时候，是否阻塞，false会抛出异常，true阻塞直到超时。默认为true。

        JedisShardInfo jedisShardInfo1 = new JedisShardInfo(redisIp6380, redisPort6380, 1000*2);
        JedisShardInfo jedisShardInfo2 = new JedisShardInfo(redisIp6381, redisPort6381, 1000*2);
        JedisShardInfo jedisShardInfo3 = new JedisShardInfo(redisIp6382, redisPort6382, 1000 * 2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(3);

        jedisShardInfoList.add(jedisShardInfo1);
        jedisShardInfoList.add(jedisShardInfo2);
        jedisShardInfoList.add(jedisShardInfo3);

        pool = new ShardedJedisPool(config,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }

    static{
        initPool();
    }

    public static ShardedJedis getJedis(){
        return pool.getResource();
    }


    public static void returnBrokenResource(ShardedJedis jedis) {

        pool.returnBrokenResource(jedis);
    }



    public static void returnResource(ShardedJedis jedis){
        pool.returnResource(jedis);
    }


    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();

        for(int i =0;i<10;i++){
            jedis.set("key"+i,"value"+i);
        }
        returnResource(jedis);

//        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");


    }
}
