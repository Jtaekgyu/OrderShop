package com.example.myshop.service;

import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.controller.dto.response.OrderResDto;

import java.util.List;

public interface OrderService {
    List<OrderResDto> memberOrderList(Long id);
    OrderResDto createOrder(OrderReqDto reqDto);
}
