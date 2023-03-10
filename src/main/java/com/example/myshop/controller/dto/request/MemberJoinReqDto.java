package com.example.myshop.controller.dto.request;

import com.example.myshop.domain.Address;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinReqDto {

    private String name;
    private Boolean verified;
    private Address address;
//    private String city;
//    private String street;
//    private String zipcode;
    @Builder
    public MemberJoinReqDto(String name, Boolean verified, Address address){
        this.name = name;
        this.verified = verified;
        this.address = address;
    }

}
