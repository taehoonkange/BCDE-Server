package com.bcdeproject.domain.boast.post.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@ApiModel(description = "게시물 수정 항목")
public class BoastPostUpdateDto {

    @ApiModelProperty(value = "수정할 게시물 제목", required = true)
    @NotBlank(message = "업데이트할 제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "수정할 게시물 내용", required = true)
    @NotBlank(message = "업데이트할 내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "수정할 게시물 해시태그 리스트", required = true)
    private List<String> hashTag;
}
