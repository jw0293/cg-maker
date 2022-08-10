package com.html.cgmaker.login.oauth.web.entity;

import com.html.cgmaker.login.BaseTimeEntity;
import com.html.cgmaker.login.domain.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "user_table")
public class User extends BaseTimeEntity implements Serializable {

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userEmail;

    @Column
    private String picturePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Builder
    public User(String userName, String userEmail, String picturePath, UserRole role) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.picturePath = picturePath;
        this.role = role;
    }

    public User update(String userName, String picturePath){
        this.userName = userName;
        this.picturePath = picturePath;

        return this;
    }

    public String getRoleKey(){
        return this.role.getKey();
    }
}
