package com.example.myshop.domain.mapper;

import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.domain.Member;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberResMapper extends GenericMapper<MemberResDto, Member> {

}
