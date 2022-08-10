package com.html.cgmaker.login.form.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.html.cgmaker.login.exception.InputNotFoundException;
import com.html.cgmaker.login.form.web.dto.LoginUserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(final AuthenticationManager authenticationManager){
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(final HttpServletRequest request, final HttpServletResponse response) throws AuthenticationException {

        final UsernamePasswordAuthenticationToken authRequest;

        try{
            final LoginUserDto loginUserDto = new ObjectMapper().readValue(request.getInputStream(), LoginUserDto.class);

            authRequest = new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword());
        } catch (IOException exception){
            throw new InputNotFoundException();
        }
        setDetails(request, authRequest);

        return this.getAuthenticationManager().authenticate(authRequest);

    }
}
