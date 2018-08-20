package  com.mall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: zhouheng
 * @Created: with IntelliJ IDEA.
 * @Description:
 * @Date: 2018-08-19
 * @Time: 20:40
 */
@Slf4j
public class CookieUtil {

    public static final String DOMAIN_NAME = ".imoocs.com";
    public static final String COOKIE_NAME = "mall_login_token";

    public static void writeLoginToken(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        cookie.setDomain(DOMAIN_NAME);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60 * 24 * 365);
        cookie.setPath("/");
        log.info("write cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());

        response.addCookie(cookie);

    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {
                log.info("read cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());
                if (StringUtils.equals(cookie.getName(), COOKIE_NAME)) {
                    log.info("read cookieName:{},cookieValue:{}",cookie.getName(),cookie.getValue());

                    return cookie.getValue();
                }

            }
        }
        return null;
    }

    public static void delLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {

            for (Cookie cookie : cookies) {
                if (StringUtils.equals(cookie.getName(), DOMAIN_NAME)) {

                    cookie.setPath("/");
                    cookie.setDomain(DOMAIN_NAME);
                    cookie.setMaxAge(0);
                    log.info("del cookieName:{},cookieValue:{}", cookie.getName(), cookie.getValue());

                    response.addCookie(cookie);
                    return;
                }
            }
        }
    }
}
