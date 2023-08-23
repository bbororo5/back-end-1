package com.example.sixneek.mypage.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {
    private String nickname;
    private String birth;
    private String gender;
    private String emoji;
    private String job;
    private String interests;
    private String password;
//    private Boolean isSubscribe;

    public void updatePassword(String encoded) {
        this.password = encoded;
    }

}