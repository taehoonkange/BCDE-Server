package com.bcdeproject.domain.boast.post.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
public class BoastPostUpdateDto {

    private Optional<String> title;
    private Optional<String> content;
    private List<String> hashTag;
    private List<MultipartFile> uploadImg;
}
