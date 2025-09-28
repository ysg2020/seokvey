package com.ysgpjt.seokvey.common;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class CookieUtil {

        private static final String ANON_COOKIE_NAME = "ANON_TOKEN";

        public static String getCookie(HttpServletRequest request, String name) {
            if (request.getCookies() == null) return null;
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
            return null;
        }

        public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
            Cookie cookie = new Cookie(name, value);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge);
            response.addCookie(cookie);
        }

        public static void deleteCookie(HttpServletResponse response, String name) {
            Cookie cookie = new Cookie(name, "");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }

        // 익명 사용자 토큰 발급
        public static String getAnonymousToken(HttpServletRequest request, HttpServletResponse response) {
            String token = getCookie(request, ANON_COOKIE_NAME);
            if (token != null) {
                return token;
            }
            String newToken = UUID.randomUUID().toString();
            addCookie(response, ANON_COOKIE_NAME, newToken, 60 * 60 * 24 * 365); // 1년
            log.info("익명 사용자 토큰 발급 : " + newToken);
            return newToken;
        }

}
