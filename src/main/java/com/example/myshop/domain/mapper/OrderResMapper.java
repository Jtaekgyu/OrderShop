package com.example.myshop.domain.mapper;

import com.example.myshop.controller.dto.response.OrderResDto;
import com.example.myshop.domain.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderResMapper extends GenericMapper<OrderResDto, Order> {
}
