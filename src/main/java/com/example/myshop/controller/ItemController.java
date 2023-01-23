package com.example.myshop.controller;

import com.example.myshop.controller.dto.request.ItemReqDto;
import com.example.myshop.controller.dto.response.ItemResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/item")
public class ItemController {

    private final ItemService itemService;

    @PostMapping // 상품등록
    public Response<ItemResDto> create(@RequestBody ItemReqDto reqDto){
        ItemResDto resDto = itemService.create(reqDto);
        return Response.success(resDto);
    }
}
