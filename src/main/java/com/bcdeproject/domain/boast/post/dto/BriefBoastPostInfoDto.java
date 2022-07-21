package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.post.BoastPost;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BriefBoastPostInfoDto {

    private Long postId;

    private String title;//제목
    private String content;//내용
    private String writerName;//작성자의 닉네임
    private String createdDate; //작성일

    public BriefBoastPostInfoDto(BoastPost post) {
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.writerName = post.getWriter().getNickName();
        this.createdDate = post.getCreatedDate().toString();
    }
}