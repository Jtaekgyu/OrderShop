package com.example.myshop.service;

import com.example.myshop.controller.dto.request.ItemReqDto;
import com.example.myshop.controller.dto.response.ItemResDto;

import java.util.List;

public interface ItemService {
    ItemResDto create(ItemReqDto reqDto);
    ItemResDto getItem(Long itemId);
    List<ItemResDto> getItemList();
}
