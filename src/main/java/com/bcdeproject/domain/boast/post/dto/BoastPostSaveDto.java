package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
public class BoastPostSaveDto {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    private List<BoastHashTag> hashTag;

//    private List<MultipartFile> uploadImg;

}
