package com.mall.service.impl;

import basic.ApplicationTest;
import com.mall.controller.protal.UserController;
import com.mall.service.IUserService;
import com.mall.util.JsonMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-30
 * @Time: 15:14
 */
public class UserServiceImplTest  extends ApplicationTest {

    @Autowired
    private IUserService userService;

    @Autowired
    private UserController userController;

    @Test
    public void login() {
        Object login = userService.login("admin", "427338237BD929443EC5D48E24FD2B1A");
        System.out.println(JsonMapper.obj2String(login));


    }

    @Test
    public void testJson() {
//        JsonData user = userController.getUser();
    }
}