package com.html.cgmaker.login.oauth.web.controller;

import com.html.cgmaker.login.oauth.web.entity.User;
import com.html.cgmaker.login.domain.dto.Token;
import com.html.cgmaker.login.oauth.web.repository.UserRepository;
import com.html.cgmaker.login.utils.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    @ResponseBody
    @GetMapping("/")
    public String index(){
        return "hello";
    }

    @ResponseBody
    @PostMapping("/user/test")
    public String hello(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Auth");
        String email = tokenUtils.getUid(token);

        return email;
    }


    @PostMapping("/admin/test")
    public String adminTest(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader("Auth");
        String email = tokenUtils.getUid(token);

        System.out.println("email = " + email);

        return "Success !!";
    }

    @ResponseBody
    @PostMapping("/login")
    public Token login(@RequestBody Map<String, String> info){
        User user = userRepository.findByUserEmail(info.get("email"))
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 E-MAIL입니다."));

        return tokenUtils.generateToken(user.getUserEmail(), "USER");
    }

    @ResponseBody
    @GetMapping("/save")
    public User saveUser(HttpServletRequest request, HttpServletResponse response){
        String token = response.getHeader("Auth");

        System.out.println("token = " + token);

        String email = tokenUtils.getUid(token);
        User user = userRepository.findByUserEmail(email).get();

        return user;
    }


}
