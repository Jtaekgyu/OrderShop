package com.example.myshop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "orders") // order가 명령어라서 orders로 이름 지음
@Getter
@NoArgsConstructor
public class Order {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id") //★ 외래키가 있는 주인 쪽에서 상대방한테 @JoinColumn을 건다
    private Member member;

    @Enumerated(EnumType.STRING) // 만약에 EnumType 이 ORDINAL 이면 새로운 타입이 추가 되면 숫자가 꼬여서 반드시 STRING으로 해야한다.
    private OrderStatus status;
}
