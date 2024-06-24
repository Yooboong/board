package com.yooboong.board.repository;

import com.yooboong.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "SELECT * " +
            "FROM COMMENT " +
            "WHERE POSTING_ID = :postingId",
            nativeQuery = true)
    List<Comment> findByPostingId(Long postingId);
}
