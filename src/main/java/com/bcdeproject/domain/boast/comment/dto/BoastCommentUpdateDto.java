package com.bcdeproject.domain.boast.comment.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Data
@NoArgsConstructor
@ApiModel(description = "게시물 댓글 수정 항목")
public class BoastCommentUpdateDto {

    @ApiModelProperty(value = "게시물 댓글 수정 내용", required = true)
    @NotBlank(message = "수정할 댓글의 내용을 입력하세요.")
    private String content;
}
