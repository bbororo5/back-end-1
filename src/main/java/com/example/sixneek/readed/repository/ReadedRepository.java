package com.example.sixneek.readed.repository;

import com.example.sixneek.readed.entity.Readed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReadedRepository extends JpaRepository<Readed, Long> {
    @Query("select (count(r) > 0) from Readed r where r.article.id = ?1 and r.member.id = ?2")
    boolean existsByArticle_IdAndMember_Id(Long id, Long id1);

    List<Readed> findByMemberId(Long memberId);
}
