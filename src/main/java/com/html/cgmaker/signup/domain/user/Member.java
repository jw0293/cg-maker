package com.html.cgmaker.signup.domain.user;

import com.html.cgmaker.signup.domain.Address;
import com.html.cgmaker.signup.domain.BaseTimeEntity;
import com.html.cgmaker.signup.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String picture;

    @Column(nullable = false)
    private String sex;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Column(nullable = false)
    private int age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

}
