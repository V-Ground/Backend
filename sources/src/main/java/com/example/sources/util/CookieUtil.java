package com.example.sources.util;

import com.example.sources.exception.EmptyCookieException;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
public class CookieUtil {

    /**
     * 쿠키 배열로부터 토큰 string 을 반환한다.
     *
     * @param cookies : Request 에 포함된 cookie 들
     * @return token string
     */
    public String getTokenFromCookies(Cookie[] cookies) {
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals("accessToken")) {
                return cookie.getValue();
            }
        }
        throw new EmptyCookieException();
    }

}
