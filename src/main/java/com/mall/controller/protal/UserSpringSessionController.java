package com.mall.controller.protal;

import com.mall.common.Const;
import com.mall.common.RequestHolder;
import com.mall.common.ResponseCode;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/userSpringSession")
@ResponseBody
public class UserSpringSessionController {

    @Autowired
    private IUserService userService;

    @RequestMapping(value = "/login.do", method = RequestMethod.GET)
    public ServerResponse login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {

        int i = 0;
        int j = 66 / i;
        ServerResponse<User> response = userService.login(username, password);
        if (response.isSuccess()) {
            //放到session中
            session.setAttribute(Const.CURRENT_USER, response.getData());

//            CookieUtil.writeLoginToken(servletResponse, session.getId());


            //放入redis中
//            RedisPoolUtil.setEx(session.getId(), JsonUtil.obj2String(response.getData()), Const.RedisCacheExtime.REDIS_SESSION_EXTIME);

        }

        return response;
    }


    @RequestMapping(value = "/get_user_info.do", method = RequestMethod.GET)
    public ServerResponse<User> getUserInfo(HttpSession session) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
       /* //二期改造
        String loginToken = CookieUtil.readLoginToken(RequestHolder.getCurrentRequest());
        if (StringUtils.isEmpty(loginToken)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(), "用户未登录，需要强制登录status=10");
        }
        String token = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(token, User.class);

        if (user == null) {
            return ServerResponse.createByErrorMessage("用户未登录,无法获取当前用户的信息");
        }*/

        return ServerResponse.createBySuccess(user);
    }

    @RequestMapping("/logout.do")
    public ServerResponse<User> logout(HttpSession session) {

        session.removeAttribute(Const.CURRENT_USER);
//        String loginToken = CookieUtil.readLoginToken(request);
//        CookieUtil.delLoginToken(request, response);
//
//        RedisPoolUtil.del(loginToken);

        return ServerResponse.createBySuccessMessage("退出成功");
    }
}
