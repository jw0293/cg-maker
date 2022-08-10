package com.html.cgmaker.login.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.domain.dto.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenUtils tokenUtils;
    private final ObjectMapper objectMapper;

    @GetMapping("/token/expired")
    public String auth(){
        throw new RuntimeException();
    }

    @PostMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String refreshToken = request.getHeader("Refresh");

        if(refreshToken != null && tokenUtils.isValidToken(refreshToken)){
            String email = tokenUtils.getUid(refreshToken);
            Token newToken = tokenUtils.generateToken(email, "USER");

            response.addHeader(AuthConstants.AUTH_HEADER, newToken.getAccessToken());
            response.addHeader(AuthConstants.REFRESH_HEADER, newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            var writer = response.getWriter();

            String allToken = objectMapper.writeValueAsString(newToken);

            return "OK NEW TOKEN";
        }

        throw new RuntimeException();

    }


}
