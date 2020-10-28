package com.site.jwt.util;

import javax.servlet.http.Cookie;

public class CookieUtils {

    /**
     * 寻找指定 cookieName 的cookie
     * @param cookies
     * @param cookieName
     * @return
     */
    public static Cookie getCookie(Cookie[] cookies, String cookieName) {
        if (null == cookies) {
            return null;
        } else {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return cookie;
                }
            }
            return null;
        }
    }
}
