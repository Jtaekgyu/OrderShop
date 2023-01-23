package com.example.myshop.controller;

import com.example.myshop.controller.dto.request.ItemReqDto;
import com.example.myshop.controller.dto.response.ItemResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    
    @GetMapping("/{itemId}") // 특정상품 조회(itemId에 맞는 item조회)
    public Response<ItemResDto> getItem(@PathVariable("itemId") Long itemId){
        return Response.success(itemService.getItem(itemId));
    }

    @GetMapping // 상품 목록 조회
    public Response<List<ItemResDto>> getItemList(){
        return Response.success(itemService.getItemList());
    }
}
