package com.yooboong.board.service;

import com.yooboong.board.dto.BoardDto;
import com.yooboong.board.entity.Board;
import com.yooboong.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public List<BoardDto> getBoardList() {
        List<Board> boardList = boardRepository.findAll();

        List<BoardDto> boardDtoList = boardList.stream()
                .map(entity -> BoardDto.toDto(entity))
                .collect(Collectors.toList());

        return boardDtoList;
    }

    public BoardDto create(BoardDto boardDto) {
        Board entity = Board.toEntity(boardDto);
        Board created = boardRepository.save(entity);

        BoardDto createdDto = BoardDto.toDto(created);
        return createdDto;
    }


    public boolean checkDuplicateName(String name) {
        Board target = boardRepository.findByName(name);
        if (target == null){
            return false;
        }
        return true;
    }

    public BoardDto getBoard(Long id) {
        Board target = boardRepository.findById(id).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("해당하는 게시판이 존재하지 않음");
        return BoardDto.toDto(target);
    }

    public BoardDto update(BoardDto boardDto) {
        Board target = boardRepository.findById(boardDto.getId()).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("게시판 수정 실패! 해당하는 게시판이 존재하지 않음");

        target.update(boardDto);
        Board updated = boardRepository.save(target);
        return BoardDto.toDto(updated);
    }

    public void delete(Long id) {
        Board target = boardRepository.findById(id).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("게시판 삭제 실패! 해당하는 게시판이 존재하지 않음");
        boardRepository.delete(target);
    }

}
