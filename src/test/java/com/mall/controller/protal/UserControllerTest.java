package com.mall.controller.protal;

import basic.ApplicationTest;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.JsonMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.util.UUID;

import static org.junit.Assert.*;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-31
 * @Time: 10:51
 */
public class UserControllerTest extends ApplicationTest {

    @Autowired
    private UserController userController;
    @Autowired
    private IUserService userService;

    @Test
    public void login() {
        ServerResponse<User> login = userService.login("admin", "admin");
        System.out.println(login);
        System.out.println(JsonMapper.obj2String(login));


    }

    @Test
    public void register() {
        User user = userService.getUserById(1);
        user.setId(null);

        user.setUsername("zhouheng");
        user.setEmail("1562@163.com");

        ServerResponse<String> register = userController.register(user);
        System.out.println(JsonMapper.obj2String(register));

    }

    @Test
    public void checkValid() {
        ServerResponse<String> response = userController.checkValid("zhouheng", "username");
        System.out.println(JsonMapper.obj2String(response));
        ServerResponse<String> responsetype = userController.checkValid("1562@163.com", "email");
        System.out.println(JsonMapper.obj2String(responsetype));
        ServerResponse<String> respons = userController.checkValid("zhouheng2", "username");

        System.out.println(JsonMapper.obj2String(respons));




    }

    @Test
    public void getUserInfo() {
    }

    @Test
    public void forgetGetQuestion() {
        ServerResponse<String> response = userController.forgetGetQuestion("zhouheng");
        System.out.println(JsonMapper.obj2String(response));

    }

    @Test
    public void logout() {

        String string = UUID.randomUUID().toString();
        System.out.println(string);

    }

    @Test
    public void forgetCheckAnswer() {
        ServerResponse<String> response = userController.forgetCheckAnswer("zhouheng", "问题", "答案");

        System.out.println(JsonMapper.obj2String(response));

    }

    @Test
    public void forgetResetPassword() {
        ServerResponse<String> response1 = userController.forgetCheckAnswer("zhouheng", "问题", "答案");

        String data = response1.getData();

        ServerResponse<String> response = userController.forgetResetPassword("zhouheng", "zhouheng", data);
        System.out.println(JsonMapper.obj2String(response));

    }

    @Test
    public void resetPassword() {
        User user = userService.getUserById(22);
        System.out.println(user);
        String passwordOld = user.getPassword();
        String passwordNew = passwordOld;
        ServerResponse<String> response = userService.resetPassword(passwordOld, passwordNew, user);

        System.out.println(JsonMapper.obj2String(response));

    }
}