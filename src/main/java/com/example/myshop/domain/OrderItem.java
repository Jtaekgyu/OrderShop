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

    @ManyToOne(fetch = FetchType.LAZY) // cascade = CascadeType.DETACH
    @JoinColumn(name = "item_id") // 단방향이라 Item 쪽에는 코드 없음
    private Item item;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private Integer orderPrice;

    private Integer quantity;

    @Builder
    public OrderItem(Item item, Order order, Integer orderPrice, Integer quantity){
        this.item = item;
        this.order = order;
        this.orderPrice = orderPrice;
        this.quantity = quantity;
    }

    // 생성 메서드
    // 도메인 모델 패턴 사용 ( DDD: Domain Driven Design 사용) : 엔티티(Entity)가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것
    public static OrderItem createOrderItem(Item item, Integer orderPrice, Integer quantity){
        // 주문 수량이 0이하이면 에러 발생시킬 수 있다.
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setQuantity(quantity);

        item.removeStock(quantity);
        return orderItem;
    }

    public long getTotalPrice(){
        return this.orderPrice * this.quantity;
    }

}
