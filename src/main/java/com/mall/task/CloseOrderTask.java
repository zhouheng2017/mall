package com.mall.task;

import com.mall.common.Const;
import com.mall.service.IOrderService;
import com.mall.util.PropertiesUtil;
import com.mall.util.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-27
 * @Time: 10:03
 */
@Component
@Slf4j
public class CloseOrderTask {

    @Autowired
    private IOrderService orderService;

    @PreDestroy
    public void delLock() {
        RedisPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

    }

    //    @Scheduled(cron = "* */1 * * * ?")
    public void closeOrderV1() {
        log.info("关闭订单定时任务启动");
        int hour = Integer.parseInt(PropertiesUtil.getProperty("time", "2"));

//        orderService.closerOrder(hour);

        log.info("关闭订单定时任务关闭");

    }


    @Scheduled(cron = "* */1 * * * ?")
    public void closeOrderV2() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));

        Long setnx = RedisPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(lockTimeout));

        if (setnx != null && setnx.intValue() == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {
            log.info("没有获得分布式锁：{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);


        }

        log.info("关闭订单定时任务关闭");

    }

//    @Scheduled(cron = "* */1 * * * ?")
    public void closeOrderV3() {
        log.info("关闭订单定时任务启动");
        long lockTimeout = Long.parseLong(PropertiesUtil.getProperty("lock.timeout", "5000"));

        Long setnx = RedisPoolUtil.setnx(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(lockTimeout));

        if (setnx != null && setnx.intValue() == 1) {
            closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        } else {

            String lockValueStr = RedisPoolUtil.get(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

            if (lockValueStr != null && System.currentTimeMillis() > Long.parseLong(lockValueStr)) {
                String getSetResult = RedisPoolUtil.getSet(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, String.valueOf(System.currentTimeMillis() + lockTimeout));

                if (getSetResult == null || (getSetResult != null && StringUtils.equals(lockValueStr, getSetResult))) {
                    closeOrder(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

                } else {
                    log.info("没有获取分锁：{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

                }
            } else {
                log.info("没有获取到分布式锁:{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);

            }

        }

        log.info("关闭订单定时任务关闭");

    }


    private void closeOrder(String lockName) {
        RedisPoolUtil.expire(lockName, 5);
        log.info("获取{}， ThreadName：{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());

        int hour = Integer.parseInt(PropertiesUtil.getProperty("close.order.task.time.hour", "2"));

//        orderService.closerOrder(hour);

        RedisPoolUtil.del(Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK);
        log.info("释放{}， THreadName：{}", Const.REDIS_LOCK.CLOSE_ORDER_TASK_LOCK, Thread.currentThread().getName());

        log.info("=============");

    }


}
