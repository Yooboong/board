package com.yooboong.board.repository;

import com.yooboong.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
//            @Query(value = "SELECT * " +
//            "FROM COMMENT " +
//            "WHERE POSTING_ID = :postingId",
//            nativeQuery = true)
    @Query("select c " +
            "from Comment c " +
            "where " +
            "c.posting.id = :postingId ")
    List<Comment> findByPostingId(@Param("postingId") Long postingId);

    @Modifying
    @Query("delete from Comment c " +
            "where " +
            "c.author.id = :authorId")
    void deleteByAuthorId(@Param("authorId") Long authorId);
}
