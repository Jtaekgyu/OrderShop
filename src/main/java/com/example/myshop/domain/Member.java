package com.example.myshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Member extends TimeStamped{

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private boolean isVerified;

    @Embedded // jpa 내장타입 - 새로운 값 타입을 직접 정의해서 사용할 수 있다. 이것을 Embedded 타입이라고 한다.
    private Address address;

    @JsonIgnore // 양 방향 연관관계에서는 명시해야한다. 안그러면 무한 루프로 참조한다
    @OneToMany(mappedBy = "member") // 양방향 연관관계의 주인이 Order에 속해있는 Member이다.
    private List<Order> orders = new ArrayList<>();

    @Builder
    public Member(String name, Address address){
        this.name = name;
        this.address = address;
    }
}
