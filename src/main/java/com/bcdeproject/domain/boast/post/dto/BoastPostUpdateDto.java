package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class BoastPostUpdateDto {

    private String title;
    private String content;
    private List<BoastHashTag> hashTag;
    private List<MultipartFile> uploadImg;
}
