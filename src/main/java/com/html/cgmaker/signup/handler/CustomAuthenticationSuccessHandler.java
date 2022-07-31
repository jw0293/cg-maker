package com.html.cgmaker.signup.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.signup.domain.dto.Token;
import com.html.cgmaker.signup.domain.dto.UserDto;
import com.html.cgmaker.signup.domain.dto.UserRequestMapper;
import com.html.cgmaker.signup.utils.TokenUtils;
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
    private final UserRequestMapper userRequestMapper;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("oAuth2User = " + oAuth2User.getAttributes().get("name"));

        UserDto userDto = userRequestMapper.toDto(oAuth2User);
        Token token = tokenUtils.generateToken(userDto.getEmail(), "USER");

        writeTokenResponse(response, token);
    }

    private void writeTokenResponse(HttpServletResponse response, Token token) throws IOException{

        response.setContentType("text/html;charset=UTF-8");

        response.addHeader("Auth", token.getAccessToken());
        response.addHeader("Refresh", token.getRefreshToken());
        response.setContentType("application/json;charset=UTF-8");

        var writer = response.getWriter();

        String allToken = objectMapper.writeValueAsString(token);
        System.out.println("allToken = " + allToken);

        // writer.println(objectMapper.writeValueAsString(token));
        writer.flush();
    }
}