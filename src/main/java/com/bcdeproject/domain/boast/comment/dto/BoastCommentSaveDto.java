package com.bcdeproject.domain.boast.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@ApiModel(description = "게시물 댓글 저장 항목")
public class BoastCommentSaveDto {

    @ApiModelProperty(value = "게시물 댓글 등록 내용", required = true)
    @NotBlank(message = "등록할 댓글의 내용을 입력하세요.")
    private String content;
}
