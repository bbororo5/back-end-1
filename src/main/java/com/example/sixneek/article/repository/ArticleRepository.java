package com.example.sixneek.article.repository;

import com.example.sixneek.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitleContainingOrContentContaining(String title, String content);       // title & content 에서 검색

    Page<Article> findByIdLessThanOrderByIdDesc(Long lastArticleId, Pageable pageable);
    Page<Article> findByTagAndIdLessThanOrderByIdDesc(String tag, Long lastArticleId, Pageable pageable);
}
