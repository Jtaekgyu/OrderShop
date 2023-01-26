package com.example.myshop.service;

import com.example.myshop.domain.Order;

import java.util.Optional;
import java.util.function.Consumer;

public class OrderProcessStep {

    private final Consumer<Order> processOrder;
    private OrderProcessStep next;

    public OrderProcessStep(Consumer<Order> processOrder) {
        this.processOrder = processOrder;
    }

    public OrderProcessStep setNext(OrderProcessStep next) {
        if (this.next == null) {
            this.next = next; // 링크드리스트의 맨 마지막에 추가
        } else {
            this.next.setNext(next);
        }
        return this;
    }

    // order를 받아서 processOrder를 실행해준다.
    public void process(Order order) {
        processOrder.accept(order);
        Optional.ofNullable(next)
                .ifPresent(nextStep -> nextStep.process(order));
    }
}
