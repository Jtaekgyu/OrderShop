package com.example.myshop.controller;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
import com.example.myshop.controller.dto.response.MemberResDto;
import com.example.myshop.controller.dto.response.Response;
import com.example.myshop.domain.Member;
import com.example.myshop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Response<MemberResDto> join(@RequestBody MemberJoinReqDto reqDto){
        MemberResDto resDto = memberService.join(reqDto);
        return Response.success(resDto);
    }

    @GetMapping
    public Response<List<MemberResDto>> memberList(){
        return Response.success(memberService.memberList());
    }
}
