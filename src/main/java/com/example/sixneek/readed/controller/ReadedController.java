package com.example.sixneek.readed.controller;

import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.readed.dto.ReadedResponseDto;
import com.example.sixneek.readed.service.ReadedService;
import com.example.sixneek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ReadedController {

    private final ReadedService readedService;

    @PostMapping("/api/articles/{articleId}/check")
    public ApiResponseDto<ReadedResponseDto> postReaded(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable Long articleId) {
        ReadedResponseDto responseDto = readedService.postReaded(userDetails, articleId);
        return new ApiResponseDto<>(HttpStatus.OK, "완독 확인", responseDto);
    }
}
