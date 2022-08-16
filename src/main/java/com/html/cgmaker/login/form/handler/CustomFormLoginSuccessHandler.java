package com.html.cgmaker.login.form.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.domain.dto.Token;
import com.html.cgmaker.login.domain.enums.UserRole;
import com.html.cgmaker.login.utils.TokenUtils;
import com.html.cgmaker.login.form.web.entity.Member;
import com.html.cgmaker.login.form.web.entity.MyUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
@RequiredArgsConstructor
public class CustomFormLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final TokenUtils tokenUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        final Member member = ((MyUserDetails) authentication.getPrincipal()).getMember();

        final Token token = tokenUtils.generateToken(member.getEmail(), UserRole.USER.getKey());
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token.getAccessToken());
        response.addHeader(AuthConstants.REFRESH_HEADER, AuthConstants.TOKEN_TYPE + " " + token.getRefreshToken());

        String tkn = new ObjectMapper().writeValueAsString(token);
        log.info("Token = {}", tkn);
    }
}
