package com.example.sixneek.like.service;

import com.example.sixneek.article.entity.Article;
import com.example.sixneek.article.repository.ArticleRepository;
import com.example.sixneek.global.dto.MsgResponseDto;
import com.example.sixneek.like.entity.Like;
import com.example.sixneek.like.repository.LikeRepository;
import com.example.sixneek.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final ArticleRepository articleRepository;
    private final LikeRepository likeRepository;

    // 게시글 좋아요 유/무 (false 면 좋아요 취소, true 면 좋아요)
    @Transactional
    public boolean checkLike(Long articleId, Member member) {
        return likeRepository.existsByArticleIdAndMemberId(articleId, member.getId());
    }

    // 게시글 좋아요 개수
    @Transactional
    public MsgResponseDto saveLike(Long articleId, Member member) {
        // 게시글이 있는지
        Article article = articleRepository.findById(articleId).orElseThrow(
                () -> new RuntimeException("에러")
        );

        if (!checkLike(articleId, member)) {
            Like like = Like.builder()
                    .article(article)
                    .member(member)
                    .build();
            likeRepository.save(like);
            return new MsgResponseDto("게시글 좋아요", HttpStatus.OK.value());
        } else {
            likeRepository.deleteByArticleIdAndMemberId(articleId, member.getId());
            return new MsgResponseDto("게시글 좋아요 취소", HttpStatus.OK.value());
        }
    }
}
