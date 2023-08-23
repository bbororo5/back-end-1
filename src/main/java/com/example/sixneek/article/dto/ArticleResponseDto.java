package com.example.sixneek.article.dto;

import com.example.sixneek.article.entity.Article;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;


@Getter
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ArticleResponseDto {
    private Long id;
    private String image;
    private String title;
    private String date;
    private String tag;
    private String content;
    private int likesCount;
    private boolean likeCheck;

    public ArticleResponseDto(Article article, boolean likeCheck) {
        this.id = article.getId();
        this.tag = article.getTag();
        this.title = article.getTitle();
        this.image = article.getImage();
        this.date =  article.getDate();
        this.content = article.getContent();
        this.likesCount = article.getLikeList().size();
        this.likeCheck = likeCheck;
    }

    public ArticleResponseDto(Long id, String tag, String title, String image, String date) {
        this.id = id;
        this.tag = tag;
        this.title = title;
        this.image = image;
        this.date = date;
    }
}
