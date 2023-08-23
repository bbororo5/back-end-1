package com.example.sixneek.member.service;

import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.member.dto.SignupRequestDto;
import com.example.sixneek.member.entity.Member;
import com.example.sixneek.member.repository.MemberRepository;
import com.example.sixneek.security.repository.RefreshTokenRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRedisRepository redisRepository;

    public ApiResponseDto<?> signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());
        String nickname = requestDto.getNickname();

        // 이메일 중복 확인
        if (memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        // 닉네임 중복 확인
        if (memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 존재하는 닉네임입니다.");
        }

        Member member = new Member(email, password, nickname);
        memberRepository.save(member);

        return ApiResponseDto.builder()
                .status(HttpStatus.CREATED)
                .message("회원가입 성공")
                .build();
    }

    public ApiResponseDto<?> withdraw(Member member) {
        memberRepository.delete(member);
        redisRepository.deleteById(member.getEmail());

        return ApiResponseDto.builder()
                .status(HttpStatus.OK)
                .message("회원탈퇴 성공")
                .build();
    }

    public ApiResponseDto<?> logout(Member member) {
        redisRepository.deleteById(member.getEmail());

        return ApiResponseDto.builder()
                .status(HttpStatus.OK)
                .message("로그아웃 성공")
                .build();
    }
}
