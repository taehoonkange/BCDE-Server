package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.hashtag.dto.BoastHashTagDto;
import com.bcdeproject.domain.boast.imgurl.dto.BoastImgUrlDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

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
    private List<BoastHashTagDto> hashTag;
}
