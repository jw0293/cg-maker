package com.html.cgmaker.login.form.web.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SignUpDto {

    private String name;
    private String email;
    private String password;
    private int age;
    private String sex;
    private String city;
}
