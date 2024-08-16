package com.yooboong.board.entity;

import com.yooboong.board.dto.BoardDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Board extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE)
    private List<Posting> postingList;

    public static Board toEntity(BoardDto boardDto) {
        return Board.builder()
                .name(boardDto.getName())
                .build();
    }

    public void update(BoardDto boardDto) {
        this.name = boardDto.getName();
    }
}
