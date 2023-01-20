package com.example.myshop.service;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.domain.Address;
import com.example.myshop.domain.Member;
import com.example.myshop.domain.mapper.MemberReqMapper;
import com.example.myshop.domain.mapper.MemberResMapper;
import com.example.myshop.exception.ErrorCode;
import com.example.myshop.exception.MyShopApplicationException;
import com.example.myshop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
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
        // 반환형이 MemberResDto.MemberResDtoBuilder 일 때 다음과 같은 에러가 발생함
        // No serializer found for class com.example.myshop.controller.dto.response.MemberResDto$MemberResDtoBuilder 
        // and no properties discovered to create BeanSerializer (to avoid exception, disable SerializationFeature.FAIL_ON_EMPTY_BEANS) 
        // (through reference chain: com.example.myshop.controller.dto.response.Response["result"]->java.util.ArrayList[0])
        /*List<MemberResDto.MemberResDtoBuilder> memberResDtoBuilders = new ArrayList<>();
        List<Member> memberList = memberRepository.findAll();
        for(Member member : memberList){
            memberResDtoBuilders.add(MemberResDto.builder()
                    .name(member.getName())
                    .verified(member.getVerified())
                    .address(member.getAddress()));
        }
        return memberResDtoBuilders;*/

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
        /*memberResDtoList = members.stream()
                .map(MemberResDto.builder()
                        .name())
                .collect(Collectors.toList());*/
        return memberResDtoList;
    }
}
