package com.mall.test;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-22
 * @Time: 21:37
 */
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

public class RedisDataSource  {

    private static ThreadLocal<ShardedJedis> jedisLocal = new ThreadLocal<ShardedJedis>();
    private static ShardedJedisPool pool;
    static {
        pool = JedisPool.getShardedJedisPool();
    }

    public  ShardedJedis getClient() {
        ShardedJedis jedis = jedisLocal.get();
        if (jedis == null) {
            jedis = pool.getResource();
            jedisLocal.set(jedis);
        }
        return jedis;
    }

    //关闭连接
    public void returnResource() {
        ShardedJedis jedis = jedisLocal.get();
        if (jedis != null) {
            pool.destroy();
            jedisLocal.set(null);
        }
    }

    public static void main(String[] args) {

        RedisDataSource redisDataSource = new RedisDataSource();
        ShardedJedis jedis = redisDataSource.getClient();


        for(int i =0;i<10;i++){
            jedis.set("key"+i,"value"+i);
        }
//        returnResource(jedis);

//        pool.destroy();//临时调用，销毁连接池中的所有连接
        System.out.println("program is end");

    }
}
