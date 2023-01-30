package com.example.myshop.service;

import com.example.myshop.controller.dto.ItemQuantityDto;
import com.example.myshop.controller.dto.request.MyOrderReqDto;
import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.controller.dto.request.OrderReqDto2;
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

import java.time.LocalDateTime;
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
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), reqDto.getQuantity());
        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        if (order.getStatus() == OrderStatus.ERROR){
            item.preserveStock(reqDto.getQuantity());
            // ERROR이면 감소로직이 생성이 안되게 해버림
//            orderRepository.flush();
        }
        orderRepository.save(order);

        OrderResDto orderResDto = new OrderResDto(member.getName(), order.getStatus(),
                member.getAddress(), order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt());
        return orderResDto;
        // MapStruct 방법
//        Order order = orderRepository.save(orderReqMapper.toEntity(reqDto));
//        return orderResMapper.toDto(order);
    }

    /*@Transactional
    public Void order(OrderReqDto2 orderReqDto){
        List<OrderItem> orderItems = new LinkedList<>();
        for(ItemQuantityDto itemQuantityDto: orderReqDto.getItemQuantityDtoList()){
            Member member = memberRepository.findById(itemQuantityDto.getMemberId()).orElseThrow(
                    () -> new MyShopApplicationException(ErrorCode.MEMBER_NOT_FOUND,
                            String.format("%s is not found",itemQuantityDto.getMemberId()))
            );
            Item item = itemRepository.findById(itemQuantityDto.getItemId()).orElseThrow(
                    () -> new MyShopApplicationException(ErrorCode.ITEM_NOT_FOUND,
                            String.format("%s is not founded", itemQuantityDto.getItemId()))
            );
            // 배송정보 생성
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            // 주문상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), itemQuantityDto.getQuantity());
            orderItems.add(orderItem);
        }
        // 주문 생성
        Order order = Order.createOrder2(orderItems);
        orderRepository.save(order);
        return null;
    }*/

    public List<OrderResDto> memberOrderList(Long memberId){
        List<Order> orderList = orderRepository.findAllByMemberId(memberId);
        List<OrderResDto> orderResDtoList = orderList.stream()
                .map(order -> new OrderResDto(order.getMember().getName(), order.getStatus(),
                        order.getMember().getAddress(), order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt()))
                .collect(Collectors.toList());

        return orderResDtoList;
    }

    public List<OrderResDto> myOrderList1(MyOrderReqDto reqDto){
        List<Order> orderList = orderRepository.findAllByMemberId(reqDto.getMemberID());
//        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        // 원본 데이터를 변경하지 않음

        // 주문이 성공하고(OrderStatus.PROCESSED)
        // 검증된 유저인지
        // reqDto.getMinutes()분 이내 주문한거
        // OrderTotalPrice가 reqDto.getTotalPrice() 이상인거를
        // List<OrderResDto> 형태로 반환한다.
        List<OrderResDto> orderResDtoList = orderList.stream()
                .filter(order -> order.getStatus() == OrderStatus.PROCESSED)
                .filter(order -> order.getMember().getVerified() == true) //검증안된 유저의 주문은 어차피 PROCESSED가 아니다.
                .filter(order -> order.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(reqDto.getMinutes())))
                .filter(order -> order.getOrderTotalPrice() >= reqDto.getTotalPrice())
                .map(order -> new OrderResDto(order.getMember().getName(), order.getStatus(), order.getMember().getAddress(),
                        order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt()))
                .collect(Collectors.toList());
        return orderResDtoList;
    }

    public Long myOrderList2(MyOrderReqDto reqDto){
        List<Order> orderList = orderRepository.findAllByMemberId(reqDto.getMemberID());
        // 주문이 성공하고(OrderStatus.PROCESSED)
        // 검증된 유저인지
        // n분 이내 주문한거의
        // order들의 TotalPrice 총합을 구함(reduce 사용)
        Long maxOrderPrice = orderList.stream()
                .filter(order -> order.getStatus() == OrderStatus.PROCESSED)
                .filter(order -> order.getMember().getVerified() == true) //검증안된 유저의 주문은 어차피 PROCESSED가 아니다.
                .filter(order -> order.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(reqDto.getMinutes())))
                .map(order -> order.getOrderTotalPrice())
                .reduce(0L, (x, y) -> x + y);

        return maxOrderPrice;
    }

    public List<String> myOrderList3(){
        List<Order> orderList = orderRepository.findAll();

        // 주문한 유저의 이름만 걸러낸다.
        List<String> orderMemberName = orderList.stream()
                .filter(order -> order.getStatus() == OrderStatus.PROCESSED)
                .filter(order -> order.getMember().getVerified() == true)
                .map(order -> order.getMember().getName())
                .distinct() // 안하면 List -> Set -> List로 바꿔야하는 번거로움
                .sorted((o1, o2) -> o1.compareTo(o2))
                .collect(Collectors.toList());

        return orderMemberName;
    }

    public long sequantialStream(MyOrderReqDto reqDto){
        long startTime;
        long endTime;
        List<Order> orderList = orderRepository.findAllByMemberId(reqDto.getMemberID());

        startTime = System.nanoTime();
        orderList.stream()
                .filter(order -> order.getStatus() == OrderStatus.PROCESSED)
                .filter(order -> order.getMember().getVerified() == true) //검증안된 유저의 주문은 어차피 PROCESSED가 아니다.
                .filter(order -> order.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(reqDto.getMinutes())))
                .filter(order -> order.getOrderTotalPrice() >= reqDto.getTotalPrice())
                .sorted((o1, o2) -> o1.getDelivery().getAddress().getCity().compareTo(o2.getDelivery().getAddress().getCity()))
                .map(order -> new OrderResDto(order.getMember().getName(), order.getStatus(), order.getMember().getAddress(),
                        order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt()))
                .forEach(this::wasteOfTime);
        endTime = System.nanoTime();
        return endTime - startTime;
    }

    public long parallelStream(MyOrderReqDto reqDto){
        long startTime;
        long endTime;
        List<Order> orderList = orderRepository.findAllByMemberId(reqDto.getMemberID());

        startTime = System.nanoTime();
        orderList.stream().parallel()
                .filter(order -> order.getStatus() == OrderStatus.PROCESSED)
                .filter(order -> order.getMember().getVerified() == true) //검증안된 유저의 주문은 어차피 PROCESSED가 아니다.
                .filter(order -> order.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(reqDto.getMinutes())))
                .filter(order -> order.getOrderTotalPrice() >= reqDto.getTotalPrice())
                .sorted((o1, o2) -> o1.getDelivery().getAddress().getCity().compareTo(o2.getDelivery().getAddress().getCity()))
                .map(order -> new OrderResDto(order.getMember().getName(), order.getStatus(), order.getMember().getAddress(),
                        order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt()))
                .forEach(this::wasteOfTime);
        endTime = System.nanoTime();
        return endTime - startTime;
    }

    public void wasteOfTime(OrderResDto resDto){
        try {
            Thread.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public long parallelSequantialTest1(){
        long startTime;
        long endTime;
        List<Order> orderList = orderRepository.findAllByMemberId(3L);

        startTime = System.nanoTime();
        orderList.stream().parallel()
                .filter(order -> order.getStatus() == OrderStatus.PROCESSED)
                .filter(order -> order.getMember().getVerified() == true) //검증안된 유저의 주문은 어차피 PROCESSED가 아니다.
                .filter(order -> order.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(100000)))
                .filter(order -> order.getOrderTotalPrice() >= 1)
                .sorted((o1, o2) -> o1.getDelivery().getAddress().getCity().compareTo(o2.getDelivery().getAddress().getCity()))
                .map(order -> new OrderResDto(order.getMember().getName(), order.getStatus(), order.getMember().getAddress(),
                        order.getOrderTotalPrice(), order.getCreatedAt(), order.getModifiedAt()));
//                .forEach(this::wasteOfTime);
        endTime = System.nanoTime();
        return endTime - startTime;
    }
}
