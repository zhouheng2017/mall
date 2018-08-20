package com.mall.common;

import com.mall.pojo.User;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-02
 * @Time: 22:16
 */
public class RequestHolder {
    public static final ThreadLocal<User> USER_THTRED_LOCAL = new ThreadLocal<>();
    public static final ThreadLocal<HttpServletRequest> REQUEST_THREAD_LOCAL = new ThreadLocal<>();

    public static void add(User user) {
        USER_THTRED_LOCAL.set(user);

    }

    public static void add(HttpServletRequest request) {
        REQUEST_THREAD_LOCAL.set(request);


    }

    public static User getCurrentUser() {
        return USER_THTRED_LOCAL.get();
    }
    public static HttpServletRequest getCurrentRequest() {
        return REQUEST_THREAD_LOCAL.get();
    }

    public static void remove() {
        USER_THTRED_LOCAL.remove();
        REQUEST_THREAD_LOCAL.remove();

    }
}
