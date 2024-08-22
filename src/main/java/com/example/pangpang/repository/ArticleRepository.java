package com.example.pangpang.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.pangpang.entity.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long>, JpaSpecificationExecutor<Article>{
    Page<Article> findByArticleTitleContaining(String title, Pageable pageable);

    Page<Article> findByMemberMemberNicknameContaining(String memberNickname, Pageable pageable);

    Page<Article> findByMemberId(Long memberId, Pageable pageable);

    // 조회수 증가 메서드
    @Modifying
    @Query("UPDATE Article a SET a.viewCount = a.viewCount + 1 WHERE a.id = :id")
    void incrementViewCount(@Param("id") Long id);
}
