package com.example.myshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order가 명령어라서 orders로 이름 지음
@Getter
@NoArgsConstructor
public class Order extends TimeStamped{

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //★ 외래키가 있는 주인 쪽에서 상대방한테 @JoinColumn을 건다
    private Member member;

    // ★★★ 참고로 CascadeType.ALL을 사용할 수 있는 이유는
    // 1. Order만 OrderItem를 참조해서 쓰기 때문이다. 즉, Order만 OrderItem, Delivery를 관리하기 때문이다.
    // 물론 OrderItem과 이 다른 것을 참조할 수 있지만 Order를 제외한 다른 곳에서 OrderItem을 참조하는 곳이 없다.
    // 2. Order와 OrderItem의 persist 라이프 사이클이 똑같기 때문이다.
    // 이럴 때만 CascadeType을 사용하면 된다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();

    // 참고로 Enumerated 타입은 Enum타입의 변수에 적용할 수 있다.
    @Enumerated(EnumType.STRING) // 만약에 EnumType 이 ORDINAL 이면 새로운 타입이 추가 되면 숫자가 꼬여서 반드시 STRING으로 해야한다.
    private OrderStatus status;

//    private LocalDateTime orderDate;
}
