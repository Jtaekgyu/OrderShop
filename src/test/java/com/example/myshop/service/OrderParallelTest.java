package com.example.myshop.service;

import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.domain.Member;
import com.example.myshop.domain.Order;
import com.example.myshop.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OrderParallelTest {

    @Autowired
    OrderService orderService;

    @MockBean
    private OrderRepository orderRepository;
    
    @Test
    @DisplayName("병렬처리와 순차처리 비교")
    void Parallel_Test(){
        // given
        Long memberId = 1L;
        Long itemId = 30L;
        Integer quantity = 3;
        OrderReqDto orderReqDto = OrderReqDto.builder()
                .memberId(memberId)
                .itemId(itemId)
                .quantity(quantity)
                .build();
//        when(orderRepository.findAllByMemberId(memberId)).thenReturn(Optional.of(mock(Member.class)));


    }

}
