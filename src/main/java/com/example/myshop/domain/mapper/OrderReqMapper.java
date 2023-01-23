package com.example.myshop.domain.mapper;

import com.example.myshop.controller.dto.request.OrderReqDto;
import com.example.myshop.domain.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderReqMapper extends GenericMapper<OrderReqDto, Order>{
}
