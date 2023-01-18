package com.example.myshop.controller;

import com.example.myshop.controller.dto.request.MemberJoinReqDto;
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
    public void join(@RequestBody MemberJoinReqDto reqDto){
        memberService.join(reqDto);
    }

//    @GetMapping
//    public List<Member> memberList(){
//        return memberService.memberList();
//    }
}
