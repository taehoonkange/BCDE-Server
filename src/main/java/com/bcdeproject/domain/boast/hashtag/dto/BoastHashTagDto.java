package com.bcdeproject.domain.boast.hashtag.dto;


import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoastHashTagDto {
    private Long id;
    private String name;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public BoastHashTagDto(BoastHashTag boastHashTag) {
        this.id = boastHashTag.getId();
        this.name = boastHashTag.getName();
        this.createdDate = boastHashTag.getCreatedDate();
        this.lastModifiedDate = boastHashTag.getLastModifiedDate();
    }
}
