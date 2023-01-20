package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.domain.Member;

import java.util.List;

public interface MemberService {

    MemberResDto join(MemberJoinReqDto reqDto);
    List<MemberResDto> memberList();
    List<MemberResDto> verifiedMemberList();
}
