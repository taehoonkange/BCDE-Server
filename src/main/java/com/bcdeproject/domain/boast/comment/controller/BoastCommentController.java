package com.bcdeproject.domain.boast.comment.controller;

import com.bcdeproject.domain.boast.comment.dto.BoastCommentSaveDto;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentUpdateDto;
import com.bcdeproject.domain.boast.comment.service.BoastCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boastComment")
public class BoastCommentController {

    private final BoastCommentService boastCommentService;

    /**
     * 댓글 저장
     */
    @PostMapping("{boastPostId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentSave(@PathVariable("boastPostId") Long boastPostId,
                            @RequestBody BoastCommentSaveDto boastCommentSaveDto) {
        boastCommentService.save(boastPostId, boastCommentSaveDto);
    }

    /**
     * 대댓글 저장
     */
    @PostMapping("{boastPostId}/{boastCommentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void reCommentSave(@PathVariable("boastPostId") Long boastPostId,
                              @PathVariable("boastCommentId") Long boastCommentId,
                              @RequestBody BoastCommentSaveDto boastCommentSaveDto) {
        boastCommentService.saveReComment(boastPostId, boastCommentId, boastCommentSaveDto);
    }

    /**
     * 댓글 업데이트
     */
    @PatchMapping("{boastCommentId}")
    public void update(@PathVariable("boastCommentId") Long boastCommentId,
                       @RequestBody BoastCommentUpdateDto boastCommentUpdateDto) {
        boastCommentService.update(boastCommentId, boastCommentUpdateDto);
    }

    /**
     * 댓글 삭제
     */
    @DeleteMapping("{boastCommentId}")
    public void delete(@PathVariable("boastCommentId") Long boastCommentId) {
        boastCommentService.remove(boastCommentId);
    }
}
