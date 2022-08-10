package com.html.cgmaker.login.form.web.entity;

import com.html.cgmaker.login.BaseTimeEntity;
import com.html.cgmaker.login.domain.enums.UserRole;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Member extends BaseTimeEntity implements Serializable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Setter
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String city;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public String getRoleKey(){
        return this.role.getKey();
    }

}
