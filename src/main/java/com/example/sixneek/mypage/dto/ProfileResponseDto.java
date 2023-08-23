package com.example.sixneek.mypage.dto;

import com.example.sixneek.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ProfileResponseDto {
    private String email;
    private String nickname;
    private String birth;
    private String gender;
    private String emoji;
    private String interests;
    private String job;
    private int likesCount;
    private int readedCount;

    public ProfileResponseDto(Member member) {
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.birth = member.getBirth();
        this.gender = member.getGender();
        this.emoji = member.getEmoji();
        this.interests = member.getInterests();
        this.job = member.getJob();
        this.likesCount = member.getLikeList().size();
        this.readedCount = member.getReadedList().size();
    }
}