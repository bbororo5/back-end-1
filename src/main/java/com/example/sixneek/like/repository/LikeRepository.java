package com.example.sixneek.like.repository;

import com.example.sixneek.like.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByArticleIdAndMemberId(Long articleId, Long id);
    void deleteByArticleIdAndMemberId(Long articleId, Long id);
}
