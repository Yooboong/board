package com.yooboong.board.service;

import com.yooboong.board.dto.CommentDto;
import com.yooboong.board.entity.Account;
import com.yooboong.board.entity.Comment;
import com.yooboong.board.entity.Posting;
import com.yooboong.board.repository.AccountRepository;
import com.yooboong.board.repository.CommentRepository;
import com.yooboong.board.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final AccountRepository accountRepository;

    private final PostingRepository postingRepository;

    private final CommentRepository commentRepository;

    public List<CommentDto> readAll(Long postingId) {
        List<Comment> comments = commentRepository.findByPostingId(postingId);

        List<CommentDto> commentDtos = comments.stream()
                .map(entity -> CommentDto.toDto(entity))
                .collect(Collectors.toList());

        return commentDtos;
    }

    @Transactional
    public CommentDto create(String username, Long postingId, CommentDto commentDto) {
        Account account = accountRepository.findByUsername(username);
        if (account == null)
            throw new IllegalArgumentException("댓글 생성 실패! 계정이 존재하지 않음");

        Posting target = postingRepository.findById(postingId).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("댓글 생성 실패! 해당하는 id의 게시물이 존재하지 않음");

        Comment commentEntity = Comment.toEntity(account, target, commentDto);

        Comment created = commentRepository.save(commentEntity);

        CommentDto createdDto = CommentDto.toDto(created);
        return createdDto;
    }

    public CommentDto read(Long id) {
        Comment comment = commentRepository.findById(id).orElse(null);
        if (comment == null)
            throw new IllegalArgumentException("댓글 조회 실패! 해당하는 id의 댓글이 존재하지 않음");

        CommentDto commentDto = CommentDto.toDto(comment);
        return commentDto;
    }

    @Transactional
    public CommentDto update(Long id, CommentDto commentDto) {
        Comment target = commentRepository.findById(id).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("댓글 수정 실패! 해당하는 id의 댓글이 존재하지 않음");

        target.update(commentDto);

        Comment updated = commentRepository.save(target);

        CommentDto updatedDto = CommentDto.toDto(updated);
        return updatedDto;
    }

    @Transactional
    public Long delete(Long id) {
        Comment target = commentRepository.findById(id).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("댓글 삭제 실패! 해당하는 id의 댓글이 존재하지 않음");

        commentRepository.delete(target);

        Long deletedCommentPostingId = target.getPosting().getId();
        return deletedCommentPostingId;
    }

}
