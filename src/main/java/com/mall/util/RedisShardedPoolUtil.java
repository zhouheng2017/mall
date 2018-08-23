package com.mall.util;


import com.mall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;
import com.mall.common.RedisShardedPool;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-22
 * @Time: 20:43
 */
@Slf4j
public class RedisShardedPoolUtil {

    public static Long expire(String key, int exTime) {

        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();

            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("错误key{}，error", key,  e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }



    public static String setEx(String key, String value, int exTime) {

        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();

            result = jedis.setex(key, exTime, value);

        } catch (Exception e) {
            log.error("错误key{}， value{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }



    public static String set(String key, String value) {

        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();

            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("错误key{}， value{} error", key, value, e);
            RedisShardedPool.returnBrokenResource(jedis);
            return result;
        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        ShardedJedis jedis = null;
        String result = null;

        try {
            jedis = RedisShardedPool.getJedis();

            result = jedis.get(key);

        } catch (Exception e) {
            log.error("错误key{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);

        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        ShardedJedis jedis = null;
        Long result = null;

        try {
            jedis = RedisShardedPool.getJedis();

            result = jedis.del(key);
        } catch (Exception e) {
            log.error("错误key{}， value{} error", key, e);
            RedisShardedPool.returnBrokenResource(jedis);

        }
        RedisShardedPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {
        ShardedJedis jedis = RedisShardedPool.getJedis();

        RedisPoolUtil.set("KeyTest", "value");
        String value = RedisPoolUtil.get("keyTest");

    }
}


