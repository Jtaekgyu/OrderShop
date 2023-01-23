package com.example.myshop.controller.dto.request;

import lombok.Getter;

@Getter
public class OrderReqDto {
    private Long memberId;
    private Long itemId;
    private Integer count;
}
