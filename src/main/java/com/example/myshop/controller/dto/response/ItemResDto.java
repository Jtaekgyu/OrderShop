package com.example.myshop.controller.dto.response;

import com.example.myshop.domain.Item.Item;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ItemResDto {

    private String name;
    private Integer price;
    private Integer stockQuantity;
    // 일단 Book외에 다른 클래스 추가 될 수 있으니까 author와 isbn은 responseDto에 추가하지 말자

    @Builder
    public ItemResDto(String name, Integer price, Integer stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    @Builder
    public ItemResDto(Item item){
        this.name = item.getName();
        this.price = item.getPrice();
        this.stockQuantity = item.getStockQuantity();
    }
}
