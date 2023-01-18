package com.example.myshop.domain.Item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // 싱글 테이블이기 때문에 db에 저장할 때 구분해야한다.
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Book extends Item{

    private String author;

    private String isbn; // International Standard Book Number

    @Builder
    public Book(String name, Integer price, Integer stockQuantity, String author, String isbn){
        super(name, price, stockQuantity);
        this.author = author;
        this.isbn = isbn;
    }
}
