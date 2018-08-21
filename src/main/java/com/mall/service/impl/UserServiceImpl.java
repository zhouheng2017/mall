package com.mall.service.impl;

import com.google.common.base.Preconditions;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.common.TokenCache;
import com.mall.dao.UserMapper;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.MD5Util;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-30
 * @Time: 14:51
 */
@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    /**
     * 用户的登陆接口
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public ServerResponse<User> login(String username, String password) {

        //判断用户是否存在
        int count = userMapper.checkUsername(username);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String md5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username, md5Password);
        //加密处理的密码
        if (user == null) {
            return ServerResponse.createByErrorMessage("密码错误");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess("登陆成功", user);

    }

    /**
     * 用户注册接口
     * @param user
     */
    @Override
    public ServerResponse<String> register(User user) {

        if (checkExistEmail(user.getEmail())) {
            return ServerResponse.createByErrorMessage("email已存在");
        }
        if (checkExistUsername(user.getUsername())) {
            return ServerResponse.createByErrorMessage("用户已存在");
        }
        user.setRole(Const.Role.ROLE_CUSTOMER);
        user.setCreateTime(new Date());
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));

        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());

        int count = userMapper.insertSelective(user);
        if (count == 0) {
            return ServerResponse.createByErrorMessage("注册失败");
        }

        return ServerResponse.createBySuccess("注册成功");
    }

    @Override
    public ServerResponse<String> checkValidate(String keyword, String type) {
        if (StringUtils.isNotBlank(type)) {

            if (Const.USERNAME.equals(type)) {


                if (checkExistUsername(keyword)) {

                    return ServerResponse.createByErrorMessage("用户已经存在");
                }
            }
            if (Const.EMAIL.equals(type)) {

                if (checkExistEmail(keyword)) {

                    return ServerResponse.createByErrorMessage("email已经存在");
                }
            }
        }else {
            return ServerResponse.createByErrorMessage("参数错误");
        }

        return ServerResponse.createBySuccessMessage("校验成功");

    }


    @Override
    public User getUserById(int i) {
        User user = userMapper.selectByPrimaryKey(i);
        return user;
    }

    private boolean checkExistEmail(String keyword) {

        return userMapper.checkEmail(keyword) > 0;
    }

    private boolean checkExistUsername(String keyword) {
        return userMapper.checkUsername(keyword) > 0;
    }

    /**
     * 获取用户的登录问题
     *
     * @param username
     * @return
     */
    @Override
    public ServerResponse<String> forgetGetQuestion(String username) {
        if (!checkExistUsername(username)) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
        String question = userMapper.selectQuestionByUsername(username);
        if (StringUtils.isBlank(question)) {

            return ServerResponse.createByErrorMessage("该用户未设置找回密码问题");
        }
        return ServerResponse.createBySuccess(question);
    }

    @Override
    public ServerResponse<String> forgetCheckAnswer(String username, String question, String answer) {
        if (answer == null) {
            return ServerResponse.createByErrorMessage("问题答案不能为空");
        }
        int count = userMapper.checkAnswer(username, question, answer);
        if (count > 0) {
            String forgetToken = UUID.randomUUID().toString();

//            TokenCache.setKey(TokenCache.TOKEN_PREFIX + username, forgetToken);

            RedisPoolUtil.setEx(Const.TOKEN_PREFIX + username, forgetToken, 60 * 60 * 12);

            return ServerResponse.createBySuccess(forgetToken);
        }



        return ServerResponse.createByErrorMessage("问题的答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String passwordNew, String forgetToken) {

        if (StringUtils.isBlank(forgetToken)) {
            return ServerResponse.createByErrorMessage("参数错误，token需要传递");
        }
        ServerResponse<String> response = checkValidate(username, Const.USERNAME);
        if (response.isSuccess()) {
            return ServerResponse.createByErrorMessage("用户不存在");
        }
//        String token = TokenCache.getKey(TokenCache.TOKEN_PREFIX + username);
        String token = RedisPoolUtil.get(Const.TOKEN_PREFIX + username);

        if (StringUtils.isBlank(token)) {
            return ServerResponse.createByErrorMessage("token无效或者过期");
        }
        if (StringUtils.equals(forgetToken, token)) {
            String md5Password = MD5Util.MD5EncodeUtf8(passwordNew);
            int count = userMapper.updatePasswordByUsername(username, md5Password);
            if (count > 0) {
                return ServerResponse.createBySuccess("修改密码成功");
            }

        } else {
            return ServerResponse.createByErrorMessage("token错误,请重新获取重置密码的token");
        }

        return ServerResponse.createByErrorMessage("修改密码失败");
    }

    @Override
    public ServerResponse<String> resetPassword(String passwordOld, String passwordNew, User user) {

        if (StringUtils.isBlank(passwordOld) || StringUtils.isBlank(passwordNew)) {

            return ServerResponse.createByErrorMessage("原密码或新密码不可以为空");
        }
        int count = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(passwordOld), user.getId());
        if (count == 0) {

            return ServerResponse.createByErrorMessage("旧密码错误");
        }

        user.setPassword(MD5Util.MD5EncodeUtf8(passwordNew));
        int resultCount = userMapper.updateByPrimaryKeySelective(user);
        if (resultCount > 0) {
            return ServerResponse.createBySuccessMessage("修改密码成功");
        }
        return ServerResponse.createByErrorMessage("密码更新失败");

    }

    @Override
    public ServerResponse<User> updateInformation(User user) {

        int resultCount = userMapper.checkEmailByUSerId(user.getEmail(), user.getId());
        if (resultCount > 0) {
            return ServerResponse.createByErrorMessage("email已经存在，请更换emial");
        }

        User currentUser = new User();

        currentUser.setId(user.getId());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPhone());
        currentUser.setQuestion(user.getQuestion());
        currentUser.setAnswer(user.getAnswer());

        int count = userMapper.updateByPrimaryKeySelective(currentUser);
        if (count > 0) {
            return ServerResponse.createBySuccess("更新个人信息成功", currentUser);
        }

        return ServerResponse.createBySuccessMessage("更新个人信息失败");
    }

    @Override
    public ServerResponse<User> getUserInformation(String username) {

        User user = userMapper.selectByUsername(username);
        if (user == null) {
            return ServerResponse.createByErrorMessage("找到不到当前用户");
        }
        user.setPassword(StringUtils.EMPTY);

        return ServerResponse.createBySuccess(user);

    }

    @Override
    public ServerResponse checkAdminRole(User user) {
        if (user != null && user.getRole() == Const.Role.ROLE_ADMIN) {

            return ServerResponse.createBySuccess();
        }

        return ServerResponse.createByError();
    }

}
