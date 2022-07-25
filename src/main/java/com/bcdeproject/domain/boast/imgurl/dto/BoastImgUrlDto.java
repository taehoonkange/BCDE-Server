package com.bcdeproject.domain.boast.imgurl.dto;

import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoastImgUrlDto {
    private Long id;
    private String imgUrl;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public BoastImgUrlDto(BoastImgUrl boastImgUrl) {
        this.id = boastImgUrl.getId();
        this.imgUrl = boastImgUrl.getImgUrl();
        this.createdDate = boastImgUrl.getCreatedDate();
        this.lastModifiedDate = boastImgUrl.getLastModifiedDate();
    }
}
