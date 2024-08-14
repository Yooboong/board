package com.yooboong.board.repository;

import com.yooboong.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("select b " +
            "from Board b " +
            "where b.name = :name")
    Board findByName(@Param("name") String name);
}
