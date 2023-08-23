package com.example.sixneek.article.entity;


import com.example.sixneek.like.entity.Like;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String image;
    private String title;
    private String date;
    @Column(length = 5000)
    private String content;
    private String tag;

    @Builder.Default
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Like> likeList = new ArrayList<>();
}
