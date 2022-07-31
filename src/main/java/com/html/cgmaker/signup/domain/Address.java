package com.html.cgmaker.signup.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;    // 도시 ex) 서울, 부산
    private String street;  // 도로명 ex)
    private String zipcode; // 상세 주소

    protected Address(){

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
