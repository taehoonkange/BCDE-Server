package com.bcdeproject.global.condition;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "게시물 검색 항목")
public class BoastPostSearchCondition {

    @ApiModelProperty(value = "검색할 게시물 제목", required = true)
    private String title;

    @ApiModelProperty(value = "등록할 게시물 내용", required = true)
    private String content;

    // private String hashTag;
}
