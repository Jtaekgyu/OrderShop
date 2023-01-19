package com.example.myshop.controller.dto.response;

import com.example.myshop.domain.Address;
import com.example.myshop.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResDto {
    private String name;
    private Boolean verified;
    private Address address;
//    private String city;
//    private String street;
//    private String zipcode;

    @Builder
    public MemberResDto(String name, Boolean verified, Address address){
        this.name = name;
        this.verified = verified;
        this.address = address;
    }

}
