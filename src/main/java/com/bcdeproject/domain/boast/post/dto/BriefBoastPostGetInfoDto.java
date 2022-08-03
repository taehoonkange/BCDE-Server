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
    private boolean isLike;

    public BriefBoastPostGetInfoDto(BoastPost post, boolean isLike) {
        this.postId = post.getId();
        // TODO : 프로필 사진 null 안 되게 바꿨을 때 수정하기
        if(post.getBoastImgUrlList().isEmpty()) this.thumbnailImg = null;
        else this.thumbnailImg = post.getBoastImgUrlList().get(0).getImgUrl();
        this.isLike = isLike;
    }
}