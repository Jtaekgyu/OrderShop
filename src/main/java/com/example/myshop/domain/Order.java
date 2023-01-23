package com.example.myshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order가 명령어라서 orders로 이름 지음
@Getter @Setter
@NoArgsConstructor
public class Order extends TimeStamped{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //★ 외래키가 있는 주인 쪽에서 상대방한테 @JoinColumn을 건다
    private Member member;

    // ★★★ 참고로 CascadeType.ALL을 사용할 수 있는 이유는
    // 1. Order만 OrderItem과 Delivery을  참조해서 쓰기 때문이다. 즉, Order만 OrderItem, Delivery를 관리하기 때문이다.
    // 물론 OrderItem과 Delivery가 다른 것을 참조할 수 있지만 Order를 제외한 다른 곳에서 OrderItem과 Delivery를 참조하는 곳이 없다.
    // 2. Order와 OrderItem의 persist 라이프 사이클이 똑같기 때문이다.
    // 이럴 때만 CascadeType을 사용하면 된다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();

    // Order만 persist하면 Delivery 까지 persist 된다. 물론 ALL이기 때문에 다른 조건들도 적용된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;


    // 참고로 Enumerated 타입은 Enum타입의 변수에 적용할 수 있다.
    @Enumerated(EnumType.STRING) // 만약에 EnumType 이 ORDINAL 이면 새로운 타입이 추가 되면 숫자가 꼬여서 반드시 STRING으로 해야한다.
    private OrderStatus status;

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        // 참고로 ...(가변인자)은 여러개의 매개변수를 받을 수 있다는 말이다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.CREATED);
        return order;
    }
}
