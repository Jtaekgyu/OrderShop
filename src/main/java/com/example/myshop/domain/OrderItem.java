package com.example.myshop.domain;

import com.example.myshop.domain.Item.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id") // 단방향이라 Item 쪽에는 코드 없음
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer orderPrice;

    private Integer count;

    // 생성 메서드
    // 도메인 모델 패턴 사용 ( DDD: Domain Driven Design 사용) : 엔티티(Entity)가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것
    public static OrderItem createOrderItem(Item item, Integer orderPrice, Integer count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
//        OrderItem orderItem = OrderItem.builder()
//                    .item(item)
//                    .orderPrice(orderPrice)
//                    .count(count)
//                    .build();

        item.removeStock(count);
        return orderItem;
    }

    @Builder
    public OrderItem(Item item, Order order, Integer orderPrice, Integer count){
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.count = count;
    }

    public long getTotalPrice(){
        return this.orderPrice * this.count;
    }

}
