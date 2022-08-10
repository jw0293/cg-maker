package com.html.cgmaker.login.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(String email){
        super(email + " NotFoundException");
    }
}
