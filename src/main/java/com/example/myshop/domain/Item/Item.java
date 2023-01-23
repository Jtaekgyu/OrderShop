package com.example.myshop.domain.Item;

import com.example.myshop.exception.ErrorCode;
import com.example.myshop.exception.MyShopApplicationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) // 싱글테이블 전략(한 테이블에 다 넣겠다)
@DiscriminatorColumn(name = "dtype") // 싱글 테이블이기 때문에 db에 저장할 때 구분해야한다.
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;

    private Integer price;

    private Integer stockQuantity;

    public Item(String name, Integer price,Integer stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
    }

    public void removeStock(Integer quantity) {
        Integer restStock = this.stockQuantity - quantity;
        if(restStock < 0) {
            throw new MyShopApplicationException(ErrorCode.NOT_ENOUGH_STOCK, String.format("Out of Stock2"));
        }
        this.stockQuantity = restStock;
    }
}
