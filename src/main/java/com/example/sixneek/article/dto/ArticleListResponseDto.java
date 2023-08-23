package com.example.sixneek.article.dto;

import com.example.sixneek.article.entity.Article;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleListResponseDto {
    private Long id;
    private String image;
    private String title;
    private String date;
    private String tag;

    public ArticleListResponseDto(Article article) {
        this.id = article.getId();
        this.image = article.getImage();
        this.title = article.getTitle();
        this.date = article.getDate();
        this.tag = article.getTag();
    }
}
