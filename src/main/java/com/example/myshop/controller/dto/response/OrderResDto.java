package com.example.myshop.controller.dto.response;

import com.example.myshop.domain.Address;
import com.example.myshop.domain.OrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OrderResDto {
    private String memeberName;
    private OrderStatus orderStatus;
    private Address address; // 이것도 필요 여부 다시 확인하자
    private long orderTotalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    // 생각 해보니까 굳이 order 1 : n orderItemResDtos 이렇게 order 엔터티에서 List<orderItem> 가 있다고 굳이 response 하지 말자
    // 애초에 주문 한건에 대해서 List<orderItem>을 반환할 필요가 없다.
//    private List<OrderItemResDto> orderItemResDtos;

    @Builder
    public OrderResDto(String memeberName, OrderStatus orderStatus, Address address,
                       long orderTotalPrice, LocalDateTime createdAt, LocalDateTime modifiedAt){
        this.memeberName = memeberName;
        this.orderStatus = orderStatus;
        this.address = address;
        this.orderTotalPrice = orderTotalPrice;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
