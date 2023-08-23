package com.example.sixneek.readed.entity;

import com.example.sixneek.article.entity.Article;
import com.example.sixneek.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Readed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "readed_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public Readed(Member member, Article article) {
        this.member = member;
        this.article = article;
    }

}
