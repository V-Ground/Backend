package com.example.sources.util;

import org.springframework.stereotype.Component;
import javax.servlet.http.Cookie;

@Component
public class CookieUtil {

    private final int ONE_DAY = 24 * 60 * 60;

    /**
     * userId 를 받아 accessToken 을 포함한 쿠키를 생성한다.
     *
     * @param accessToken : accessToken
     * @return Cookie
     */
    public Cookie generateCookie(String accessToken) {

        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(ONE_DAY);

        return cookie;
    }

    /**
     * 쿠키의 만료 시간을 0으로 만들어 쿠키를 없앤다.
     *
     * @return max-age 가 0 인 쿠키
     */
    public Cookie destroy() {
        Cookie cookie = new Cookie("accessToken", "");
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);

        return cookie;
    }
}
