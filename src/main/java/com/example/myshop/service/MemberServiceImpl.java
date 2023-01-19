package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.domain.Address;
import com.example.myshop.domain.Member;
import com.example.myshop.domain.mapper.MemberReqMapper;
import com.example.myshop.domain.mapper.MemberResMapper;
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
    private final MemberReqMapper memberReqMapper;
    private final MemberResMapper memberResMapper;

    @Transactional
    public MemberResDto join(MemberJoinReqDto reqDto) {
        memberRepository.findByName(reqDto.getName()).ifPresent(it -> {
            throw new IllegalArgumentException();
        });

        Member member = memberRepository.save(memberReqMapper.toEntity(reqDto));
        return memberResMapper.toDto(member);
        
        // 아래는 MapStruct적용하지 않은 방법 MemberResDto 생성자로 반환하는 방법
        /*Member member = Member.builder()
                .name(reqDto.getName())
                .verified(reqDto.getVerified())
                .address(new Address(reqDto.getCity(), reqDto.getStreet(), reqDto.getZipcode()))
                .build();

        memberRepository.save(member);
        MemberResDto resDto = new MemberResDto(member);
        return resDto;*/
    }

//    public List<MemberResDto> memberList(){
//        List<MemberResDto> memberResDtoList = new ArrayList<>();
//        return memberResDtoList;
//    }
}
