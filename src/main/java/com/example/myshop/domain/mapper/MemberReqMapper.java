package com.example.myshop.domain.mapper;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.domain.Member;
import org.mapstruct.Mapper;

// @Mapper : MapStruct Code Generator가 해당 인터페이스의 구현체를 생성해준다.
// componentModel = "spring" : spring에 맞게 bean으로 등록해준다
@Mapper(componentModel = "spring")
public interface MemberReqMapper extends GenericMapper<MemberJoinReqDto, Member> {

}
