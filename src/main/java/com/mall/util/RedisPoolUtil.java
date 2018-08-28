package com.mall.util;

import com.mall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-19
 * @Time: 18:37
 */
@Slf4j
public class RedisPoolUtil {


    public static Long expire(String key, int exTime) {

        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("错误key{}，error", key,  e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }



    public static String setEx(String key, String value, int exTime) {

        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);

        } catch (Exception e) {
            log.error("错误key{}， value{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }



    public static String set(String key, String value) {

        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("错误key{}， value{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String getSet(String key, String value) {

        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.getSet(key, value);
        } catch (Exception e) {
            log.error("getset key{}， value{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long setnx(String key, String value) {

        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.setnx(key, value);
        } catch (Exception e) {
            log.error("setnx key{}， value{} error", key, value, e);
            RedisPool.returnBrokenResource(jedis);
            return result;
        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);

        } catch (Exception e) {
            log.error("错误key{} error", key, e);
            RedisPool.returnBrokenResource(jedis);

        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;

        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("错误key{}， value{} error", key, e);
            RedisPool.returnBrokenResource(jedis);

        }
        RedisPool.returnResource(jedis);
        return result;
    }

    public static void main(String[] args) {

        RedisPoolUtil.set("zhouhengKeyRedis", "zhouhengValueRedis");
        RedisPoolUtil.setEx("time", "time", 60 * 20);
        RedisPoolUtil.expire("zhouhengKeyRedis", 60 * 20);
        RedisPoolUtil.del("time");
    }

}
