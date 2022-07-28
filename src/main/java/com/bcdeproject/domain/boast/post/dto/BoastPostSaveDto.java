package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.hashtag.dto.BoastHashTagDto;
import com.bcdeproject.domain.boast.imgurl.dto.BoastImgUrlDto;
import com.bcdeproject.domain.boast.post.BoastPost;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "게시물 저장 항목")
public class BoastPostSaveDto {

    @ApiModelProperty(value = "등록할 게시물 제목", required = true)
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @ApiModelProperty(value = "등록할 게시물 내용", required = true)
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @ApiModelProperty(value = "등록할 게시물 해시태그 리스트", required = true)
    private List<String> hashTag;

}
