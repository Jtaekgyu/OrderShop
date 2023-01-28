package com.example.myshop.controller.dto.request;

import lombok.Getter;

@Getter
public class MyOrderReqDto {

    private Integer minutes;
    private Long totalPrice;
    private Long memberID;

}
