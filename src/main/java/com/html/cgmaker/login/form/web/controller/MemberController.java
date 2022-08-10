package com.html.cgmaker.login.form.web.controller;

import com.html.cgmaker.login.domain.constants.AuthConstants;
import com.html.cgmaker.login.form.web.dto.MemberDto;
import com.html.cgmaker.login.form.web.entity.Member;
import com.html.cgmaker.login.form.web.dto.SignUpDto;
import com.html.cgmaker.login.form.web.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String hello(HttpServletRequest request, HttpServletResponse response){
        String token = request.getHeader(AuthConstants.AUTH_HEADER);
        System.out.println("token = " + token);
        return "Hello !!";
    }

    @PostMapping("/signUp")
    public ResponseEntity<MemberDto> signUp(@RequestBody final SignUpDto signUpDto){
        boolean isPresent = memberService.findByEmail(signUpDto.getEmail()).isPresent();

        if(isPresent){
            return ResponseEntity.badRequest().build();
        } else{
            MemberDto dto = memberService.signUp(signUpDto);
            return ResponseEntity.ok(dto);
        }
    }

    @GetMapping("/information")
    public String test(){

        return "OK!!!!!!!";
    }
}
