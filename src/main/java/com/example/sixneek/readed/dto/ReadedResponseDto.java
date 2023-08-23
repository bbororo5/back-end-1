package com.example.sixneek.readed.dto;

import lombok.Getter;

@Getter
public class ReadedResponseDto {
    private Long articleId;
    private Long memberId;

    public ReadedResponseDto(Long articleId, Long memberId) {
        this.articleId = articleId;
        this.memberId = memberId;
    }
}
