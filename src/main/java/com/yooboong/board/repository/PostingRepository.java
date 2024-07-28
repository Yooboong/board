package com.yooboong.board.repository;

import com.yooboong.board.entity.Posting;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostingRepository extends JpaRepository<Posting, Long> {
    @Query("select p " +
            "from Posting p " +
            "where " +
            "p.title like %:keyword% " +
            "order by " +
            "p.createdDate desc")
    Page<Posting> findPageByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
