package com.example.myshop.controller.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OrderReqDto {
    private Long memberId;
    private Long itemId;
    private Integer quantity;

    @Builder
    public OrderReqDto(Long memberId, Long itemId, Integer quantity){
        this.memberId = memberId;
        this.itemId = itemId;
        this.quantity = quantity;
    }
}
