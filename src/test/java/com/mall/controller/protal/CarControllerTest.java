package com.mall.controller.protal;

import basic.ApplicationTest;
import com.mall.common.ServerResponse;
import com.mall.util.JsonMapper;
import com.mall.vo.CartVo;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-05
 * @Time: 16:48
 */
public class CarControllerTest extends ApplicationTest {

    @Autowired
    private CartController cartController;

    @Test
    public void list() {
        ServerResponse<CartVo> list = cartController.list();
        System.out.println(JsonMapper.obj2String(list));

    }
}