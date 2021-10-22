package com.example.sources.filter;

import com.example.sources.domain.entity.Role;
import com.example.sources.security.UserAuthentication;
import com.example.sources.service.AuthenticationService;
import com.example.sources.util.CookieUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class AuthenticationFilter extends BasicAuthenticationFilter {

    private final AuthenticationService authenticationService;
    private final CookieUtil cookieUtil;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AuthenticationService authenticationService,
                                CookieUtil cookieUtil) {
        super(authenticationManager);
        this.authenticationService = authenticationService;
        this.cookieUtil = cookieUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        if(request.getCookies() != null) {
            String accessToken = cookieUtil.getTokenFromCookies(request.getCookies());
            Long userId = authenticationService.parseToken(accessToken);
            List<Role> roles = authenticationService.getRoles(userId);

            Authentication customAuthentication = new UserAuthentication(
                    userId,
                    roles
            );

            SecurityContext context = SecurityContextHolder.getContext();
            context.setAuthentication(customAuthentication);
        }

        chain.doFilter(request, response);
    }
}
