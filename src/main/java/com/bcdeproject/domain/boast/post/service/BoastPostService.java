package com.bcdeproject.domain.boast.post.service;

import com.bcdeproject.global.condition.BoastPostSearchCondition;
import com.bcdeproject.domain.boast.post.dto.BoastPostInfoDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostPagingDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostSaveDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostUpdateDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoastPostService {
    /**
     * 게시글 등록
     */
    void save(BoastPostSaveDto postSaveDto, List<MultipartFile> uploadImg) throws Exception;

    /**
     * 게시글 수정
     */
    void update(Long id, BoastPostUpdateDto postUpdateDto, List<MultipartFile> updateImg) throws Exception;

    /**
     * 게시글 삭제
     */
    void delete(Long id);

    /**
     * 게시글 1개 조회
     */
    BoastPostInfoDto getPostInfo(Long id);


    /**
     * 검색 조건에 따른 게시글 리스트 조회 + 페이징
     */
    BoastPostPagingDto getPostList(Pageable pageable, BoastPostSearchCondition postSearchCondition);
}

