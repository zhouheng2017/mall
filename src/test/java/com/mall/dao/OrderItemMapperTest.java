package com.mall.dao;

import basic.ApplicationTest;
import com.mall.pojo.Order;
import com.mall.pojo.OrderItem;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-18
 * @Time: 17:31
 */
public class OrderItemMapperTest extends ApplicationTest {
    @Autowired
    private OrderItemMapper orderItemMapper;


    @Test
    public void getbyOrderNo() {
        List<OrderItem> orderItemList = orderItemMapper.getbyOrderNo(1491753014256L);


        System.out.println(orderItemList+"***************************************");

    }
}