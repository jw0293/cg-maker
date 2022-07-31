package com.html.cgmaker.signup.error.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/error")
public class ErrorController {

    @GetMapping("/unauthorized")
    public ResponseEntity<Void> unauthorized(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
