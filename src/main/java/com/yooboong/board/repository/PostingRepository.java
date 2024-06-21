package com.yooboong.board.repository;

import com.yooboong.board.entity.Posting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostingRepository extends JpaRepository<Posting, Long> {
}
