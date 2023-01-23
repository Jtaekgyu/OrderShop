package com.example.myshop.service;

import com.example.myshop.controller.dto.request.ItemReqDto;
import com.example.myshop.controller.dto.response.ItemResDto;
import com.example.myshop.domain.Item.Book;
import com.example.myshop.domain.Item.Item;
import com.example.myshop.exception.ErrorCode;
import com.example.myshop.exception.MyShopApplicationException;
import com.example.myshop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService{

    private final ItemRepository itemRepository;
//    private final ItemReqMapper itemReqMapper;
//    private final ItemResMapper itemResMapper;

    @Transactional
    public ItemResDto create(ItemReqDto reqDto){
        // 이건 Name이 없으면 방생하는 에러이다.
        /*itemRepository.findByName(reqDto.getName()).orElseThrow(
                () -> new MyShopApplicationException(ErrorCode.DUPLICATED_ITEM_NAME,
                        String.format("%s is duplicated", reqDto.getName()))
        );*/
        // 이게 Name이 존재 하면 발생하는 에러
        itemRepository.findByName(reqDto.getName()).ifPresent(it -> {
            throw new MyShopApplicationException(ErrorCode.DUPLICATED_ITEM_NAME,
                        String.format("%s is duplicated", reqDto.getName()));
        });

        Book book = Book.builder()
                .name(reqDto.getName())
                .price(reqDto.getPrice())
                .stockQuantity(reqDto.getStockQuantity())
                .author(reqDto.getAuthor())
                .isbn(reqDto.getIsbn())
                .build();

//        Item item = book;
//        itemRepository.save(item);
        Item item = itemRepository.save(book);
        ItemResDto resDto = new ItemResDto(item);
        return resDto;
        // Mapstruct 적용방식
//        Item item = itemRepository.save(itemReqMapper.toEntity(reqDto));
//        return itemResMapper.toDto(item);
    }
}
