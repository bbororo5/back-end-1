package com.example.sixneek.mypage.service;


import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.member.entity.Member;
import com.example.sixneek.member.repository.MemberRepository;
import com.example.sixneek.mypage.dto.ProfileRequestDto;
import com.example.sixneek.mypage.dto.ProfileResponseDto;
import com.example.sixneek.readed.entity.Readed;
import com.example.sixneek.readed.repository.ReadedRepository;
import com.example.sixneek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// 데이터베이스 연동, 예외 처리, 인증 및 권한 검사 등의 기능을 추가
@Service
@RequiredArgsConstructor

public class MypageService {

    private final PasswordEncoder passwordEncoder;
    private final ReadedRepository readedRepository;

    // 닉네임, 이메일, 읽었음 개수, 좋았슴 개수
    public ProfileResponseDto getMyProfile(Member member) {
        return ProfileResponseDto.builder()
                .nickname(member.getNickname())
                .email(member.getEmail())
                .readedCount(member.getReadedList().size())
                .likesCount(member.getLikeList().size())
                .build();
    }

    // 닉네임, 출생년도, 성별, (배송지), 이모지, 직업, 관심분야, 이메일, (시사 뉴스레터 구독여부) 조회
    public ProfileResponseDto getMyProfileSetting(Member member) {
        return new ProfileResponseDto(member);
    }


    @Transactional
    public ApiResponseDto<?> updateProfile(Member member, String tab, ProfileRequestDto requestDto) {
        if (tab.equals("password")) {
            String encoded = passwordEncoder.encode(requestDto.getPassword());
            requestDto.updatePassword(encoded);
        }
        String newValue = member.update(tab, requestDto);

        return ApiResponseDto.builder()
                .status(HttpStatus.OK)
                .message(tab + "값이 " + newValue + "로 변경되었습니다.")
                .build();
    }

    public ApiResponseDto<?> getReadedList(UserDetailsImpl userDetails) {
        Long memberId = userDetails.getMember().getId();
        List<Readed> read = readedRepository.findByMemberId(memberId);

        return ApiResponseDto.builder()
                .status(HttpStatus.OK)
                .message("끝까지 읽었슴 조회 성공")
                .data(read)
                .build();
    }
}
