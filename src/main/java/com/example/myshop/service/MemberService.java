package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.domain.Member;

import java.util.List;

public interface MemberService {

    void join(MemberJoinReqDto reqDto);

//    List<Member> memberList();
}
