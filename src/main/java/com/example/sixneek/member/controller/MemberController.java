package com.example.sixneek.member.controller;

import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.member.dto.SignupRequestDto;
import com.example.sixneek.member.service.MemberService;
import com.example.sixneek.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ApiResponseDto<?> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return memberService.signup(requestDto);
    }

    @DeleteMapping("/withdraw")
    public ApiResponseDto<?> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.withdraw(userDetails.getMember());
    }

    @DeleteMapping("/logout")
    public ApiResponseDto<?> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return memberService.logout(userDetails.getMember());
    }

    @GetMapping("/test")
    public String test(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails.getMember().getEmail();
    }
}
