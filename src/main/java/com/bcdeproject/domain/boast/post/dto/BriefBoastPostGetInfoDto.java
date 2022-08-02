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
    private String thumbnailImg;
    private int likeCount;
    private boolean isLike;

    public BriefBoastPostGetInfoDto(BoastPost post, int likeCount, boolean isLike) {
        this.postId = post.getId();
        this.thumbnailImg = post.getBoastImgUrlList().get(0).getImgUrl();
        this.likeCount = likeCount;
        this.isLike = isLike;
    }
}