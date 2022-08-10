package com.html.cgmaker.login.form.web.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data @Builder
public class MemberDto {

    private String name;
    private int age;
    private String city;
    private String sex;
}
