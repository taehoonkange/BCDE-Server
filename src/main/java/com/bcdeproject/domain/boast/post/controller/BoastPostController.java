package com.bcdeproject.domain.boast.post.controller;

import com.bcdeproject.global.condition.BoastPostSearchCondition;
import com.bcdeproject.domain.boast.post.dto.BoastPostSaveDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostUpdateDto;
import com.bcdeproject.domain.boast.post.service.BoastPostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boastPost")
public class BoastPostController {

    private final BoastPostService boastPostService;

    /**
     * 게시글 저장
     */
    @Operation(summary = "자랑 게시물 저장 API", description = "자랑 게시물 저장 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void save(@Valid @RequestPart BoastPostSaveDto boastPostSaveDto,
                     @RequestPart(required = false) @ApiParam(value = "게시물 저장 이미지들") List<MultipartFile> uploadImg) throws Exception {

        boastPostService.save(boastPostSaveDto, uploadImg);
    }



    /**
     * 게시글 수정
     */
    @Operation(summary = "자랑 게시물 수정 API", description = "자랑 게시물 수정 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @ResponseStatus(HttpStatus.OK)
    @PatchMapping("/{boastPostId}")
    public void update(@PathVariable("boastPostId") @ApiParam(value = "자랑 게시물 식별 ID") Long postId,
                       @RequestPart BoastPostUpdateDto boastPostUpdateDto,
                       @RequestPart(required = false) @ApiParam(value = "수정할 이미지들") List<MultipartFile> updateImg) throws Exception {

        boastPostService.update(postId, boastPostUpdateDto, updateImg);
    }

    /**
     * 게시글 삭제
     */
    @Operation(summary = "자랑 게시물 삭제 API", description = "자랑 게시물 삭제 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{boastPostId}")
    public void delete(@PathVariable("boastPostId") @ApiParam(value = "자랑 게시물 식별 ID") Long postId){
        boastPostService.delete(postId);
    }


    /**
     * 게시글 조회
     */
    @Operation(summary = "자랑 게시물 조회 API", description = "자랑 게시물 조회 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @GetMapping("/{boastPostId}")
    public ResponseEntity getInfo(@PathVariable("boastPostId") @ApiParam(value = "자랑 게시물 식별 ID") Long postId){
        return ResponseEntity.ok(boastPostService.getPostInfo(postId));
    }

    /**
     * 게시글 검색
     */
    @Operation(summary = "자랑 게시물 검색 API", description = "자랑 게시물 검색 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @GetMapping("/search")
    public ResponseEntity search(Pageable pageable,
                                 @ModelAttribute BoastPostSearchCondition boastPostSearchCondition){

        return ResponseEntity.ok(boastPostService.searchPostList(pageable,boastPostSearchCondition));
    }

    /**
     * 메인 페이지 최신 게시글 불러오기
     */
    @Operation(summary = "최신 게시물 불러오기 API", description = "최신 게시물 불러오기 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @GetMapping("/recentPost")
    public ResponseEntity getRecentPost() {

        return ResponseEntity.ok(boastPostService.getRecentPostList());
    }

    /**
     * 좋아요 추가
     */
    @PostMapping("/addLike/{boastPostId}")
    public void addLike(@PathVariable Long boastPostId) {
        boastPostService.addLike(boastPostId);
    }

    /**
     * 좋아요 삭제
     */
    @PostMapping("/deleteLike/{boastPostId}")
    public void deleteLike(@PathVariable Long boastPostId) {
        boastPostService.deleteLike(boastPostId);
    }
}