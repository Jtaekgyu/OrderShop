package com.example.myshop.service;

import com.example.myshop.controller.dto.request.ItemReqDto;
import com.example.myshop.controller.dto.response.ItemResDto;

public interface ItemService {
    ItemResDto create(ItemReqDto reqDto);
}
