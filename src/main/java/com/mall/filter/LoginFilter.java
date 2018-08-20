package com.mall.filter;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.mall.common.*;
import com.mall.pojo.User;
import com.mall.service.IUserService;
import com.mall.util.CookieUtil;
import com.mall.util.JsonUtil;
import com.mall.util.RedisPoolUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 22:13
 */
@WebFilter(filterName = "LoginFilter", urlPatterns = {"/manage/*",
        "/user/*", "/cart/*", "/shipping/*","/order/*"})
@Slf4j
public class LoginFilter implements Filter {
    public static Set<String> extensionUrl = Sets.newConcurrentHashSet();


    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String requestURI = request.getRequestURI();
        if (extensionUrl.contains(requestURI)) {
            chain.doFilter(request, response);
            return;

        }
//        User user = (User) request.getSession().getAttribute(Const.CURRENT_USER);

        String loginToken = CookieUtil.readLoginToken(request);

        String token = RedisPoolUtil.get(loginToken);

        User user = JsonUtil.string2Obj(token, User.class);


        if (user == null) {
            log.error("用户未登录");
            response.sendRedirect("/index.jsp");
            return;
        }

        if (!(user.getRole() == Const.Role.ROLE_ADMIN)) {
            
            log.error("没有权限");
            response.sendRedirect("/index.jsp");

            return;

        }
        RequestHolder.add(user);
        RequestHolder.add(request);

        chain.doFilter(request, response);
        return;
    }


    @Override
    public void init(FilterConfig config) throws ServletException {

        extensionUrl.add("/manager/login.do");
        extensionUrl.add("/user/login.do");
        extensionUrl.add("/user/logout.do");
        extensionUrl.add("/order/alipay_callback.do");
        extensionUrl.add("/");
    }

}
