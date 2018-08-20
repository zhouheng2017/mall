package com.mall.service;

import com.mall.common.ServerResponse;
import com.mall.param.UserParam;
import com.mall.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-30
 * @Time: 14:46
 */
public interface IUserService {

    /**
     * 用户的登陆接口
     *
     * @param username
     * @param password
     * @return
     */
    ServerResponse<User> login(String username, String password);


    ServerResponse<String> register(User user);

    ServerResponse<String> checkValidate(String keyword, String type);


    User getUserById(int i);

    /**
     * 获取用户的登录问题
     * @param username
     * @return
     */
    ServerResponse<String> forgetGetQuestion(String username);

    ServerResponse<String> forgetCheckAnswer(String username, String question, String answer);

    ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken);

    ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user);

    ServerResponse<User> updateInformation( User user);


    ServerResponse<User> getUserInformation(String username);

    ServerResponse checkAdminRole(User user);
}
