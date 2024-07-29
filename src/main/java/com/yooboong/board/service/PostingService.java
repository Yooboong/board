package com.yooboong.board.service;

import com.yooboong.board.dto.PostingDto;
import com.yooboong.board.entity.Posting;
import com.yooboong.board.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostingService {

    private final PostingRepository postingRepository;

    public Page<PostingDto> getPage(int page, String keyword, int searchOption) {
        Pageable pageable = PageRequest.of(page - 1, 10); // PageRequest 클래스에 실제로 들어갈 페이지 번호는 0부터

        Page<Posting> entityPage = null;

        switch (searchOption){
            case 1:
                entityPage = postingRepository.findPageByTitleKeyword(keyword, pageable);
                break;
            case 2:
                entityPage = postingRepository.findPageByTitleAndContentKeyword(keyword, pageable);
                break;
            case 3:
//                entityPage = postingRepository.findPageByAuthorKeyword(keyword, pageable);
                break;
        }

//        List<PostingDto> dtoList = new ArrayList<>();
//        List<Posting> entityList = entityPage.getContent();
//        for (int i = 0; i < entityList.size(); i++){
//            dtoList.add(PostingDto.toDto(entityList.get(i)));
//        }
//        return new PageImpl<>(dtoList, pageable, entityPage.getTotalElements());

        Page<PostingDto> dtoPage = entityPage.map(entity -> PostingDto.toDto(entity));
        return dtoPage;
    }

    @Transactional
    public PostingDto create(PostingDto postingDto) {
        Posting postingEntity = Posting.toEntity(postingDto);

        Posting created = postingRepository.save(postingEntity);

        PostingDto createdDto = PostingDto.toDto(created);
        return createdDto;
    }

    public PostingDto read(Long id) {
        Posting posting = postingRepository.findById(id).orElse(null);
        if (posting == null)
            throw new IllegalArgumentException("게시글 조회 실패! 해당하는 id의 게시물이 존재하지 않음");

        PostingDto postingDto = PostingDto.toDto(posting);
        return postingDto;
    }

    @Transactional
    public PostingDto update(Long id, PostingDto postingDto) {
        Posting target = postingRepository.findById(id).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("게시글 수정 실패! 해당하는 id의 게시물이 존재하지 않음");

        target.update(postingDto);

        Posting updated = postingRepository.save(target);

        PostingDto updatedDto = PostingDto.toDto(updated);
        return updatedDto;
    }

    @Transactional
    public void delete(Long id) {
        Posting target = postingRepository.findById(id).orElse(null);
        if (target == null)
            throw new IllegalArgumentException("게시글 삭제 실패! 해당하는 id의 게시물이 존재하지 않음");

        postingRepository.delete(target);
    }

}
