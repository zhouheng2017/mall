package com;

import com.mall.controller.common.RedisPool;
import org.codehaus.jackson.annotate.JsonSubTypes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.ShardedJedis;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-22
 * @Time: 21:59
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})

public class RedisPoolTest {
    @Autowired
    private RedisPool redisPool;

    @Test
    public void testRedisPool() {
        ShardedJedis resource = redisPool.getResource();

        for (int i = 0; i < 10; i++) {
            resource.set("key" + i, "value" + i);

        }
    }
}
