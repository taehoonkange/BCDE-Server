package com.bcdeproject.domain.boast.comment.controller;

import com.bcdeproject.domain.boast.comment.dto.BoastCommentSaveDto;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentUpdateDto;
import com.bcdeproject.domain.boast.comment.service.BoastCommentService;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "자랑 게시물 댓글 저장 API", description = "자랑 게시물 댓글 저장 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PostMapping("{boastPostId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void commentSave(@PathVariable("boastPostId") @ApiParam(value = "자랑 게시물 식별 ID") Long boastPostId,
                            @RequestBody BoastCommentSaveDto boastCommentSaveDto) {
        boastCommentService.save(boastPostId, boastCommentSaveDto);
    }

    /**
     * 대댓글 저장
     */
    @Operation(summary = "자랑 게시물 대댓글 저장 API", description = "자랑 게시물 대댓글 저장 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PostMapping("{boastPostId}/{boastCommentId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void reCommentSave(@PathVariable("boastPostId") @ApiParam(value = "자랑 게시물 식별 ID") Long boastPostId,
                              @PathVariable("boastCommentId") @ApiParam(value = "자랑 게시물 댓글 식별 ID") Long boastCommentId,
                              @RequestBody BoastCommentSaveDto boastCommentSaveDto) {
        boastCommentService.saveReComment(boastPostId, boastCommentId, boastCommentSaveDto);
    }

    /**
     * 댓글 업데이트
     */
    @Operation(summary = "자랑 게시물 댓글 수정 API", description = "자랑 게시물 댓글 수정 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PatchMapping("{boastCommentId}")
    public void update(@PathVariable("boastCommentId") @ApiParam(value = "자랑 게시물 댓글 식별 ID") Long boastCommentId,
                       @RequestBody BoastCommentUpdateDto boastCommentUpdateDto) {
        boastCommentService.update(boastCommentId, boastCommentUpdateDto);
    }

    /**
     * 댓글 삭제
     */
    @Operation(summary = "자랑 게시물 댓글 삭제 API", description = "자랑 게시물 댓글 삭제 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @DeleteMapping("{boastCommentId}")
    public void delete(@PathVariable("boastCommentId") @ApiParam(value = "자랑 게시물 댓글 식별 ID") Long boastCommentId) {
        boastCommentService.remove(boastCommentId);
    }
}
