package com.bcdeproject.domain.boast.hashtag.dto;


import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@ApiModel(description = "게시물 해시태그 항목")
public class BoastHashTagDto {

    @ApiModelProperty(value = "게시물 해시태그 식별 ID(자동 생성, 요청 X)")
    private Long id;

    @ApiModelProperty(value = "게시물 해시태그 Name", required = true)
    @NotBlank(message = "해시태그를 입력해주세요.")
    private String name;

    @ApiModelProperty(value = "게시물 해시태그 생성 시간(자동 생성, 요청 X)")
    private LocalDateTime createdDate;

    @ApiModelProperty(value = "게시물 해시태그 마지막 수정 시간(자동 생성, 요청 X)")
    private LocalDateTime lastModifiedDate;

    public BoastHashTagDto(BoastHashTag boastHashTag) {
        this.id = boastHashTag.getId();
        this.name = boastHashTag.getName();
        this.createdDate = boastHashTag.getCreatedDate();
        this.lastModifiedDate = boastHashTag.getLastModifiedDate();
    }
}
