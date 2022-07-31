package com.html.cgmaker.signup.config.web.controller;

import com.html.cgmaker.signup.domain.dto.Token;
import com.html.cgmaker.signup.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class TokenController {

    private final TokenUtils tokenUtils;

    @GetMapping("/token/expired")
    public String auth(){
        throw new RuntimeException();
    }

    @GetMapping("/token/refresh")
    public String refreshAuth(HttpServletRequest request, HttpServletResponse response){

        String refreshToken = request.getHeader("Refresh");

        System.out.println("refreshToken = " + refreshToken);

        if(refreshToken != null && tokenUtils.isValidToken(refreshToken)){
            String email = tokenUtils.getUid(refreshToken);
            Token newToken = tokenUtils.generateToken(email, "USER");

            response.addHeader("Auth", newToken.getAccessToken());
            response.addHeader("Refresh", newToken.getRefreshToken());
            response.setContentType("application/json;charset=UTF-8");

            return "OK NEW TOKEN";
        }

        throw new RuntimeException();

    }


}
