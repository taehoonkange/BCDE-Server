package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.post.BoastPost;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoastPostGetPagingDto {

    private int totalPageCount;//총 몇페이지가 존재하는지
    private int currentPageNum;//현재 몇 페이지인지
    private long totalElementCount; //존재하는 게시글의 총 개수
    private int currentPageElementCount; //현재 페이지에 존재하는 게시글 수


    private List<BriefBoastPostGetInfoDto> simpleLectureDtoList = new ArrayList<>();


    public BoastPostGetPagingDto(Page<BoastPost> searchResults) {
        this.totalPageCount = searchResults.getTotalPages();
        this.currentPageNum = searchResults.getNumber();
        this.totalElementCount = searchResults.getTotalElements();
        this.currentPageElementCount = searchResults.getNumberOfElements();
        this.simpleLectureDtoList = searchResults.getContent().stream().map(BriefBoastPostGetInfoDto::new).collect(Collectors.toList());
    }
}