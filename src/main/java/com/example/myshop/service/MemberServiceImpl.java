package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.domain.Member;
import com.example.myshop.domain.mapper.MemberReqMapper;
import com.example.myshop.domain.mapper.MemberResMapper;
import com.example.myshop.exception.ErrorCode;
import com.example.myshop.exception.MyShopApplicationException;
import com.example.myshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final MemberReqMapper memberReqMapper;
    private final MemberResMapper memberResMapper;

    @Transactional
    public MemberResDto join(MemberJoinReqDto reqDto) {
        memberRepository.findByName(reqDto.getName()).ifPresent(it -> {
            throw new MyShopApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", reqDto.getName()));
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

    public List<MemberResDto> memberList(){
        List<Member> members = memberRepository.findAll();
        List<MemberResDto> memberResDtoList = new ArrayList<>();

        // 1.Builder 사용해서 반환하는 방법
        /*for(Member member : members){
            MemberResDto resDto = MemberResDto.builder()
                    .name(member.getName())
                    .verified(member.getVerified())
                    .address(member.getAddress())
                    .build();

            memberResDtoList.add(resDto);
        }*/
        // 2. stream 사용해서 반환하는 방법
        memberResDtoList = members.stream()
                .map(member -> new MemberResDto(member.getName(), member.getVerified(), member.getAddress()))
                .collect(Collectors.toList());

        return memberResDtoList;
    }

    public List<MemberResDto> verifiedMemberList(){
        // 1. Spring Data JPA로 조회하기
        /*List<Member> members = memberRepository.findAllByVerifiedTrue();
        List<MemberResDto> memberResDtoList = new ArrayList<>();

        memberResDtoList = members.stream()
                .map(member -> new MemberResDto(member.getName(), member.getVerified(), member.getAddress()))
                .collect(Collectors.toList());*/
        List<Member> members = memberRepository.findAll();
        List<MemberResDto> memberResDtoList = new ArrayList<>();

        memberResDtoList = members.stream()
                .filter(member -> member.getVerified())
                .map(member -> new MemberResDto(member.getName(), member.getVerified(), member.getAddress()))
                .collect(Collectors.toList());

        return memberResDtoList;
    }

}
