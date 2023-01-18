package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.domain.Address;
import com.example.myshop.domain.Member;
import com.example.myshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    public void join(MemberJoinReqDto reqDto) {
        Member member = Member.builder()
                .name(reqDto.getName())
                .address(new Address(reqDto.getCity(), reqDto.getStreet(), reqDto.getZipcode()))
                .build();

        memberRepository.save(member);
    }

//    public List<Member> memberList(){
//        List<Member> memberList = new ArrayList<>();
//        return memberList;
//    }
}
