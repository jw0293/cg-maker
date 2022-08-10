package com.html.cgmaker.login.oauth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.domain.dto.Token;
import com.html.cgmaker.login.oauth.web.dto.UserDto;
import com.html.cgmaker.login.oauth.web.dto.UserRequestMapper;
import com.html.cgmaker.login.oauth.web.service.UserService;
import com.html.cgmaker.login.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenUtils tokenUtils;
    private final UserService userService;
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        UserDto userDto = userRequestMapper.toDto(oAuth2User);
        Token token = tokenUtils.generateToken(userDto.getEmail(), "USER");

        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException{

        response.setContentType("text/html;charset=UTF-8");

        response.addHeader(AuthConstants.AUTH_HEADER, token.getAccessToken());
        response.addHeader(AuthConstants.REFRESH_HEADER, token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");
    }
}