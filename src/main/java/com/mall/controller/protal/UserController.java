package com.mall.controller.protal;

import com.mall.common.*;
import com.mall.param.UserParam;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-07-30
 * @Time: 14:31
 */
@Controller
@RequestMapping("/user")
@ResponseBody
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login.do", method = RequestMethod.POST)
    public ServerResponse login(HttpServletRequest request, HttpServletResponse servletResponse, @RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {

        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            //放到session中
//            session.setAttribute(Const.CURRENT_USER, response.getData());

            CookieUtil.writeLoginToken(servletResponse, session.getId());


            //放入redis中
            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }

        return response;
    }

    @RequestMapping(value = "/register.do", method = RequestMethod.POST)
    public ServerResponse<String> register(User user) {

        return userService.register(user);
    }

    @RequestMapping(value = "/check_valid.do", method = RequestMethod.POST)
    public ServerResponse<String> checkValid(@RequestParam("str") String keyword, @RequestParam("type") String type) {
        return userService.checkValidate(keyword, type);
    }

    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.POST)
    public ServerResponse<User> getUserInfo(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }

        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping(value = "/forget_get_question.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetGetQuestion(String username) {


        return userService.forgetGetQuestion(username);

    }

    @RequestMapping(value = "/forget_check_answer.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetCheckAnswer(@RequestParam("username") String username,
                                                    @RequestParam("question") String question, @RequestParam("answer") String answer) {
        return userService.forgetCheckAnswer(username, question, answer);
    }

    @RequestMapping(value = "/forget_reset_password.do", method = RequestMethod.POST)
    public ServerResponse<String> forgetResetPassword(@RequestParam("username") String username,
                                                      @RequestParam("passwordNew") String passwordNew, @RequestParam("forgetToken") String forgetToken) {

        return userService.forgetResetPassword(username, passwordNew, forgetToken);

    }

    /**
     * 登录状态的重置密码
     *
     * @param passwordOld
     * @param passwordNew
     * @return
     */
    @RequestMapping(value = "/reset_password.do", method = RequestMethod.POST)
    public ServerResponse<String> resetPassword(@RequestParam("passwordOld") String passwordOld,
                                                @RequestParam("passwordNew") String passwordNew, HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录");
        }

        return userService.resetPassword(passwordOld, passwordNew, user);
    }

    @RequestMapping(value = "/update_information.do", method = RequestMethod.POST)
    public ServerResponse<User> updateInformation(User user, HttpSession session) {

        User userCurrent = (User) session.getAttribute(Const.CURRENT_USER);
        if (userCurrent == null) {

            return ServerResponse.createByErrorMessage("用户未登录");
        }
        user.setId(userCurrent.getId());
        user.setUsername(userCurrent.getUsername());

        ServerResponse<User> response = userService.updateInformation(user);
        if (response.isSuccess()) {
            response.getData().setUsername(userCurrent.getUsername());
            session.setAttribute(Const.CURRENT_USER, response.getData());

        }

        return response;
    }

    @RequestMapping(value = "/get_information.do", method = RequestMethod.GET)
    public ServerResponse<User> getInformation(HttpSession session) {
//        User user = (User) session.getAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(RequestHolder.getCurrentRequest());
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，需要强制登录status=10");
        }
        String token = RedisPoolUtil.get(session.getId());

        User user = JsonUtil.string2Obj(token, User.class);


        if (user == null) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，需要强制登录status=10");
        }

        return userService.getUserInformation(user.getUsername());
    }

    @RequestMapping("/logout.do")
    public ServerResponse<User> logout(HttpServletResponse response) {

//        session.removeAttribute(Const.CURRENT_USER);
        String loginToken = CookieUtil.readLoginToken(RequestHolder.getCurrentRequest());
        CookieUtil.delLoginToken(RequestHolder.getCurrentRequest(), response);

        RedisPoolUtil.del(loginToken);

        return ServerResponse.createBySuccessMessage("退出成功");
    }
}
