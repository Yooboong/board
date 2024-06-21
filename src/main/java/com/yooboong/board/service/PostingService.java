package com.yooboong.board.service;

import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.entity.Posting;
import com.yooboong.board.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    public List<PostingDto> readAll() {
        List<Posting> postingEntities = postingRepository.findAll();

        List<PostingDto> postingDtos = postingEntities.stream()
                .map(entity -> PostingDto.toDto(entity))
                .collect(Collectors.toList());

        return postingDtos;
    }

    @Transactional
    public PostingDto create(PostingDto postingDto) {
        Posting posting = Posting.toEntity(postingDto);

        Posting created = postingRepository.save(posting);

        PostingDto createdDto = PostingDto.toDto(created);
        return createdDto;
    }

    public PostingDto read(Long id) {
        Posting posting = postingRepository.findById(id).orElse(null);
        if (posting == null)
            return null;

        PostingDto postingDto = PostingDto.toDto(posting);
        return postingDto;
    }

    @Transactional
    public PostingDto update(PostingDto postingDto) {
        Posting target = postingRepository.findById(postingDto.getId()).orElse(null);

        target.patch(postingDto);

        Posting updated = postingRepository.save(target);

        PostingDto updatedDto = PostingDto.toDto(updated);
        return updatedDto;
    }

    @Transactional
    public PostingDto delete(Long id) {
        Posting target = postingRepository.findById(id).orElse(null);
        if (target == null)
            return null;

        postingRepository.delete(target);

        PostingDto deletedDto = PostingDto.toDto(target);
        return deletedDto;
    }
}
