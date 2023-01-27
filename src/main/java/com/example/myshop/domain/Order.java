package com.example.myshop.domain;

import com.example.myshop.exception.ErrorCode;
import com.example.myshop.exception.MyShopApplicationException;
import com.example.myshop.service.OrderProcessStep;
import lombok.Builder;
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

    // ★★★ 참고로 CascadeType.ALL을 사용할 수 있는 이유는 (상위 엔터티에서 하위 엔터티로 모든 작업을 전파한다)
    // 1. Order만 OrderItem과 Delivery을  참조해서 쓰기 때문이다. 즉, Order만 OrderItem, Delivery를 관리하기 때문이다.
    // 물론 OrderItem과 Delivery가 다른 것을 참조할 수 있지만 Order를 제외한 다른 곳에서 OrderItem과 Delivery를 참조하는 곳이 없다.
    // 2. Order와 OrderItem, Delivery의 persist 라이프 사이클이 똑같기 때문이다.
    // 이럴 때만 CascadeType.ALL 을 사용하면 된다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    List<OrderItem> orderItems = new ArrayList<>();

    // Order만 persist하면 Delivery 까지 persist 된다. 물론 ALL이기 때문에 다른 조건들도 적용된다.
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery;
    
    // 참고로 Enumerated 타입은 Enum타입의 변수에 적용할 수 있다.
    @Enumerated(EnumType.STRING) // 만약에 EnumType 이 ORDINAL 이면 새로운 타입이 추가 되면 숫자가 꼬여서 반드시 STRING으로 해야한다.
    private OrderStatus status;

    private long orderTotalPrice;

    @Builder
    public Order (Member member, Delivery delivery, OrderStatus status, List<OrderItem> orderItems){
        this.member = member;
        this.delivery = delivery;
        this.status = status;
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 도메인 모델 패턴 사용
    // ( DDD: Domain Driven Design 사용) : 엔티티(Entity)가 비즈니스 로직을 가지고 객체 지향의 특성을 적극 활용하는 것
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        // 참고로 ...(가변인자)은 말 그대로 인자의 개수가 변한다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderItems){
            // 추가
            order.orderTotalPrice += orderItem.getTotalPrice();
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.CREATED);
        order.orderProcessStepMethod(order);

        return order;
    }

    /*public static Order createOrder2(List<OrderItem> orderItems){
        Order order = new Order();
        for(OrderItem orderItem: orderItems){
            order.setMember(orderItem.getOrder().getMember());
            order.setDelivery(orderItem.getOrder().getDelivery());
            order.orderTotalPrice += orderItem.getTotalPrice();
            order.addOrderItem(orderItem);
            order.setStatus(OrderStatus.CREATED);
            order.orderProcessStepMethod(order);
        }
        return order;
    }*/


    public void orderProcessStepMethod(Order order){
        // tmpOrder는 onsumer<Order> 타입으로 받기 때문에 ops.getStatus() 같은게 가능하다.
        OrderProcessStep initializeStep = new OrderProcessStep(tmpOrder -> {
            if(tmpOrder.getStatus() == OrderStatus.CREATED) {
                System.out.println("Start processing order " + tmpOrder.getId());
                tmpOrder.setStatus(OrderStatus.IN_PROGRESS);
            }
        });

        // totalPrice가 맞는지 체크한다.
        OrderProcessStep totalPriceCheck = new OrderProcessStep(tmpOrder ->{
            if(tmpOrder.getStatus() == OrderStatus.IN_PROGRESS) {
                System.out.println("Checking orderTotalPrice " + tmpOrder.getId());
                if(tmpOrder.getOrderTotalPrice() < 0 )
                    tmpOrder.setStatus(OrderStatus.ERROR);
            }
        });

        // 검증된 유저인지 체크한다.
        OrderProcessStep verifyOrderStep = new OrderProcessStep(tmpOrder -> {
            if(tmpOrder.getStatus() == OrderStatus.IN_PROGRESS) {
                System.out.println("Verifying order " + tmpOrder.getId());
                if(!tmpOrder.getMember().getVerified()) // 검증된 유저가 아니면
                    tmpOrder.setStatus(OrderStatus.ERROR); // 에러발생
            }
        });

        // ERROR상태인 ORDER를 handle해주는 step , ERROR throw 하지말고 흘러보내자
        OrderProcessStep handleErrorStep = new OrderProcessStep(tmpOrder -> {
            if (tmpOrder.getStatus() == OrderStatus.ERROR) {
                System.out.println("Sending out 'Failed to process order' alert for order " + tmpOrder.getId());
                return;
                //                throw new MyShopApplicationException(ErrorCode.ERROR_OCCUR,
//                        String.format("%s is Error Occured", tmpOrder.getMember()));
            }
        });

        // IN_PROGRESS를 PROCESSED상태로 변환해 주는 스텝
        OrderProcessStep processedStep = new OrderProcessStep(tmpOrder -> {
            if(tmpOrder.getStatus() == OrderStatus.IN_PROGRESS) {
                System.out.println("Processed Step " + tmpOrder.getId());
                tmpOrder.setStatus(OrderStatus.PROCESSED);
            }
        });

        // order 완료 step
        OrderProcessStep completeProcessingOrderStep = new OrderProcessStep(tmpOrder -> {
            if(tmpOrder.getStatus() == OrderStatus.PROCESSED) {
                System.out.println("Finished processing order " + tmpOrder.getId());
                // 이곳에서 추가적으로 작업을 진행 할 수 있다. ex) 사용자에게 명세서 이메일을 보낸다든가
            }
        });

        // chain으로 엮어서 워크플로우를 만듦 , 각 스텝은 자기가 프로세스할 수 있는것만 프로세스하기 때문에
        OrderProcessStep chainedOrderProcessSteps = initializeStep
                .setNext(totalPriceCheck)
                .setNext(verifyOrderStep)
                .setNext(handleErrorStep)
                .setNext(processedStep) // error스텝이 들어가 있어도 order가 error상태가 아니면 스킵한다.
                .setNext(completeProcessingOrderStep);

        chainedOrderProcessSteps.process(order);
    }
}
