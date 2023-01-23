package com.example.myshop.controller.dto.request;

import lombok.Getter;

@Getter
public class ItemReqDto {
    private String name;
    private Integer price;
    private Integer stockQuantity;
    private String author;
    private String isbn;
}
