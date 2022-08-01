package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.hashtag.dto.BoastHashTagDto;
import com.bcdeproject.domain.boast.imgurl.dto.BoastImgUrlDto;
import com.bcdeproject.domain.boast.post.BoastPost;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BriefBoastPostGetInfoDto {

    private Long postId;

    private String title;//제목
    private String content;//내용
    private String writerName;//작성자의 닉네임
    private String createdDate; //작성일
    private List<BoastHashTagDto> hashTagList;
    private List<BoastImgUrlDto> imgUrlList;

    public BriefBoastPostGetInfoDto(BoastPost post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerName = post.getWriter().getNickName();
        this.createdDate = post.getCreatedDate().toString();
        this.hashTagList = post.getBoastHashTagList().stream()
                .map(boastHashTag -> new BoastHashTagDto(boastHashTag))
                .collect(Collectors.toList());
        this.imgUrlList = post.getBoastImgUrlList().stream()
                .map(boastImgUrl -> new BoastImgUrlDto(boastImgUrl))
                .collect(Collectors.toList());
    }
}