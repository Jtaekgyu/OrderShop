package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MyOrderReqDto;
import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.controller.dto.response.OrderResDto;

import java.util.List;

public interface OrderService {
    OrderResDto createOrder(OrderReqDto reqDto);
//    Void order(OrderReqDto2 orderReqDto2);
    List<OrderResDto> memberOrderList(Long id);
    List<OrderResDto> myOrderList1(MyOrderReqDto reqDto);
    Long myOrderList2(MyOrderReqDto reqDto);
    List<String> myOrderList3();
    long sequantialStream(MyOrderReqDto reqDto);
    long parallelStream(MyOrderReqDto reqDto);
    void wasteOfTime(OrderResDto resDto);
    long parallelSequantialTest1();
}
