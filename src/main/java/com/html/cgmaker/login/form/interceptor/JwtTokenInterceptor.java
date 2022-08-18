package com.html.cgmaker.login.form.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.domain.dto.Token;
import com.html.cgmaker.login.domain.enums.UserRole;
import com.html.cgmaker.login.utils.CookieUtils;
import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.form.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils;
    private final CookieUtils cookieUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Cookie accessCookie = cookieUtils.getCookie(request, AuthConstants.AUTH_HEADER);
        String accessToken = accessCookie.getValue();

        if(accessToken != null && tokenUtils.isValidToken(accessToken)){
            return true;
        }

        Cookie refreshCookie = cookieUtils.getCookie(request, AuthConstants.REFRESH_HEADER);
        String refreshToken = refreshCookie.getValue();

        if(refreshToken != null && tokenUtils.isValidToken(refreshToken)) {
            Cookie cookie = tokenUtils.reissueAccessToken(response, refreshToken);

            response.addCookie(cookie);
            response.setContentType("application/json;charset=UTF-8");

            return true;
        }

        response.sendRedirect("/error/unauthorized");
        return false;
    }

}
