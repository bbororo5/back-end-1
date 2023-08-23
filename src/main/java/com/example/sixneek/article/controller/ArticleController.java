package com.example.sixneek.article.controller;

import com.example.sixneek.article.dto.ArticleResponseDto;
import com.example.sixneek.article.service.ArticleService;
import com.example.sixneek.crawling.CrawlingArticle;
import com.example.sixneek.global.dto.ApiResponseDto;
import com.example.sixneek.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final CrawlingArticle crawlingArticle;

    // 크롤링: 전체 기사 조회
    @GetMapping("/crawling")
    public ApiResponseDto<?> saveArticleList() throws IOException {
        return new ApiResponseDto<>(HttpStatus.OK, "OK", crawlingArticle.saveArticleList());
    }

    // 검색 기능
    @GetMapping("/search")
    public ApiResponseDto<?> searchKeyword(@RequestParam("keyword") String keyword) {
        return new ApiResponseDto<>(HttpStatus.OK, "OK", crawlingArticle.searchKeyword(keyword));
    }


    // 전체&섹터별 기사 조회 (더보기)
    @GetMapping
    public ApiResponseDto<List<ArticleResponseDto>> getArticles(@RequestParam(value = "tag", required = false) String tag,
                                                                @RequestParam(value = "size", defaultValue = "12") Integer size,
                                                                @RequestParam(value = "page", defaultValue = "1") Integer page) {
        List<ArticleResponseDto> articles = articleService.getArticles(tag, size, page);
        return new ApiResponseDto<>(HttpStatus.OK, "기사 조회 성공", articles);
    }

    // 기사 상세 조회
    @GetMapping("/{id}")
    public ApiResponseDto<ArticleResponseDto> getArticle(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ArticleResponseDto responseDto = articleService.getArticle(id, Optional.ofNullable(userDetails));
        return new ApiResponseDto<>(HttpStatus.OK, "기사 상세조회 성공", responseDto);
    }
}
