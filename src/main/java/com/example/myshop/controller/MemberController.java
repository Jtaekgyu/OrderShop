package com.example.myshop.controller;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping // 멤버등록
    public Response<MemberResDto> join(@RequestBody MemberJoinReqDto reqDto){
        MemberResDto resDto = memberService.join(reqDto);
        return Response.success(resDto);
    }

    @GetMapping // 멤버목록 조회하기
    public Response<List<MemberResDto>> memberList(){
        return Response.success(memberService.memberList());
    }

    @GetMapping("/verification") // 검증된 멤버 목록만 조회하기
    public Response<List<MemberResDto>> verifiedMemberList(){
        return Response.success(memberService.verifiedMemberList());
    }
}
