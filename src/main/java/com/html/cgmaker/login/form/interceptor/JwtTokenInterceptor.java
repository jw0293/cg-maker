package com.html.cgmaker.login.form.interceptor;

import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.form.web.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenInterceptor implements HandlerInterceptor {

    private final TokenUtils tokenUtils;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Auth");

        if(token != null && tokenUtils.isValidToken(token)){
            return true;
        }
        response.sendRedirect("/error/unauthorized");
        return false;
    }
}
