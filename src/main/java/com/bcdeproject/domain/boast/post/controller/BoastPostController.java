package com.bcdeproject.domain.boast.post.controller;

import com.bcdeproject.domain.boast.post.condition.BoastPostSearchCondition;
import com.bcdeproject.domain.boast.post.dto.BoastPostSaveDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostUpdateDto;
import com.bcdeproject.domain.boast.post.service.BoastPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class BoastPostController {

    private final BoastPostService boastPostService;


    /**
     * 게시글 저장
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/post")
    public void save(@Valid @RequestBody BoastPostSaveDto boastPostSaveDto) throws Exception {
        boastPostService.save(boastPostSaveDto);
    }

    /**
     * 게시글 수정
     */
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/post/{postId}")
    public void update(@PathVariable("postId") Long postId,
                       @RequestBody BoastPostUpdateDto boastPostUpdateDto) throws Exception {

        boastPostService.update(postId, boastPostUpdateDto);
    }

    /**
     * 게시글 삭제
     */
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/post/{postId}")
    public void delete(@PathVariable("postId") Long postId){
        boastPostService.delete(postId);
    }


    /**
     * 게시글 조회
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity getInfo(@PathVariable("postId") Long postId){
        return ResponseEntity.ok(boastPostService.getPostInfo(postId));
    }

    /**
     * 게시글 검색
     */
    @GetMapping("/post/search")
    public ResponseEntity search(Pageable pageable,
                                 @RequestBody BoastPostSearchCondition boastPostSearchCondition){

        return ResponseEntity.ok(boastPostService.getPostList(pageable,boastPostSearchCondition));
    }
}