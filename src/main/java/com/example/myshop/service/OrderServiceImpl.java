package com.example.myshop.service;

import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.controller.dto.response.OrderResDto;
import com.example.myshop.domain.*;
import com.example.myshop.domain.Item.Item;
import com.example.myshop.domain.mapper.OrderReqMapper;
import com.example.myshop.domain.mapper.OrderResMapper;
import com.example.myshop.exception.ErrorCode;
import com.example.myshop.exception.MyShopApplicationException;
import com.example.myshop.repository.ItemRepository;
import com.example.myshop.repository.MemberRepository;
import com.example.myshop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final OrderReqMapper orderReqMapper;
    private final OrderResMapper orderResMapper;

    @Transactional
    public OrderResDto createOrder(OrderReqDto reqDto){
        // 엔터티 조회
        Member member = memberRepository.findById(reqDto.getMemberId()).orElseThrow(
                () -> new MyShopApplicationException(ErrorCode.MEMBER_NOT_FOUND,
                        String.format("%s is not founded", reqDto.getMemberId()))
        );
        Item item = itemRepository.findById(reqDto.getItemId()).orElseThrow(
                () -> new MyShopApplicationException(ErrorCode.ITEM_NOT_FOUND,
                        String.format("%s is not founded", reqDto.getItemId()))
        );

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), reqDto.getCount());
        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        // 여기 orderProcessStep 추가하자?

        orderRepository.save(order);
        OrderResDto orderResDto = new OrderResDto(member.getName(), order.getStatus(),
                member.getAddress(), order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt());
        return orderResDto;
        // MapStruct 방법
//        Order order = orderRepository.save(orderReqMapper.toEntity(reqDto));
//        return orderResMapper.toDto(order);
    }

    public List<OrderResDto> memberOrderList(Long memberId){
        List<Order> orderList = orderRepository.findAllByMemberId(memberId);
        List<OrderResDto> orderResDtoList = orderList.stream()
                .map(order -> new OrderResDto(order.getMember().getName(), order.getStatus(),
                        order.getMember().getAddress(), order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt()))
                .collect(Collectors.toList());

        return orderResDtoList;
    }
}
