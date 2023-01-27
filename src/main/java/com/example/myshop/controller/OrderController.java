package com.example.myshop.controller;

import com.example.myshop.controller.dto.request.MyOrderReqDto;
import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.controller.dto.response.OrderResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping // 주문하기
    public Response<OrderResDto> order(@RequestBody OrderReqDto reqDto){
        return Response.success(orderService.createOrder(reqDto));
    }

    /*@PostMapping("/list") // 여러개 주문하기
    public Response<Void> orderlist(@RequestBody OrderReqDto2 reqDto2){
        return Response.success(orderService.order(reqDto2));
    }*/

    @GetMapping("/{id}") // 멤버id로 주문 조회 // 최적화 안해서 지금 n+1 문제 발생할 거다.
    public Response<List<OrderResDto>> memberOrderList(@PathVariable("id") Long memberId){
        return Response.success(orderService.memberOrderList(memberId));
    }

    @GetMapping("/my1")
    public Response<List<OrderResDto>> myOrderList1(@RequestBody MyOrderReqDto reqDto){
        return Response.success(orderService.myOrderList1(reqDto));
    }

    @GetMapping("/my2")
    public Response<Long> myOrderList2(@RequestBody MyOrderReqDto reqDto){
        return Response.success(orderService.myOrderList2(reqDto));
    }

}
