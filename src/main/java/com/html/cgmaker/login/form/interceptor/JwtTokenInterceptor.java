package com.html.cgmaker.login.form.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.domain.dto.Token;
import com.html.cgmaker.login.domain.enums.UserRole;
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

        String accessToken = request.getHeader(AuthConstants.AUTH_HEADER);
        if(accessToken != null && tokenUtils.isValidToken(accessToken)){
            return true;
        }

        String refreshToken = request.getHeader(AuthConstants.REFRESH_HEADER);
        if(refreshToken != null && tokenUtils.isValidToken(refreshToken)) {
            String email = tokenUtils.getUid(refreshToken);
            Token newToken = tokenUtils.generateToken(email, UserRole.USER.getKey());

            response.addHeader(AuthConstants.AUTH_HEADER, newToken.getAccessToken());
            response.addHeader(AuthConstants.REFRESH_HEADER, newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            log.info("New AccessToken : " + newToken.getAccessToken());
            log.info("New RefreshToken : " + newToken.getRefreshToken());

            return true;
        }

        response.sendRedirect("/error/unauthorized");
        return false;
    }
}
