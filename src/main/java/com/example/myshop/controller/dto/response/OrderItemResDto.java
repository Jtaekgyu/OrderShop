package com.example.myshop.controller.dto.response;

import lombok.Getter;

@Getter
public class OrderItemResDto {
//    private ItemResDto itemResDto; // 이거 없어도 될듯? 아니구나 Item name은 필요할 듯
    private String itemName;
    private Integer orderPrice;
    private Integer quantity;

}
