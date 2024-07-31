package com.yooboong.board.repository;

import com.yooboong.board.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    @Query("select p " +
            "from Posting p " +
            "where " +
            "p.title like %:keyword% " +
            "order by " +
            "p.createdDate desc")
    Page<Posting> findPageByTitleKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p " +
            "from Posting p " +
            "where " +
            "p.title like %:keyword% or " +
            "p.content like %:keyword% " +
            "order by " +
            "p.createdDate desc")
    Page<Posting> findPageByTitleAndContentKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("select p " +
            "from Posting p " +
            "where " +
            "p.author.nickname like %:keyword% " +
            "order by " +
            "p.createdDate desc")
    Page<Posting> findPageByAuthorKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Modifying // JPQL 쿼리가 DB 수정 작업(삽입, 업데이트, 삭제)을 할수있도록 함
    @Query("update " +
            "Posting p " +
            "set " +
            "p.view = p.view + 1 " +
            "where " +
            "p.id = :id")
    void updateView(@Param("id") Long id);
}
