package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.hashtag.dto.BoastHashTagDto;
import com.bcdeproject.domain.boast.imgurl.dto.BoastImgUrlDto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class BoastPostUpdateDto {

    @NotBlank(message = "업데이트할 제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "업데이트할 내용을 입력해주세요.")
    private String content;

    private List<BoastHashTagDto> hashTag;
}
