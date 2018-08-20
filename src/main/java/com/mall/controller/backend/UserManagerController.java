package com.mall.controller.backend;

import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-01
 * @Time: 13:45
 */
@Controller
@RequestMapping("/manager")
public class UserManagerController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/login.do")
    public ServerResponse<User> login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {


        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            User user = response.getData();
            if (user.getRole() == Const.Role.ROLE_ADMIN) {
                session.setAttribute(Const.CURRENT_USER, user);
                return response;
            } else {

                return ServerResponse.createByErrorMessage("不是管理员无法登录");
            }
        }
        return response;

    }
}
