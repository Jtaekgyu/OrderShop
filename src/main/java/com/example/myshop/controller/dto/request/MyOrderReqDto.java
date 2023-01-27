package com.example.myshop.controller.dto.request;

import lombok.Getter;

@Getter
public class MyOrderReqDto {

    private Integer minutes;
    private long totalPrice;
    private Long memberID;

}
