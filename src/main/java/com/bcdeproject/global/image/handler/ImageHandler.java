package com.bcdeproject.global.image.handler;

import com.bcdeproject.global.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * List<MultipartFile>을 받아서 List<String>로 반환하는 Handler
 * 실제 파일 -> 파일 경로로 변경 + 반환 타입 String으로 변환
 */
@Component
@RequiredArgsConstructor
public class ImageHandler {

    private final ImageService imageService;

    /**
     * 이미지 리스트 경로로 변환
     */
    public List<String> parseFileListInfo(List<MultipartFile> multipartFiles) throws Exception {

        // 반환을 할 이미지 리스트
        List<String> imgUrlList = new ArrayList<>();

        // 이미지가 빈 것이 들어오면 빈 것을 반환
        if (multipartFiles.isEmpty()) {
            return imgUrlList;
        }

        for (MultipartFile multipartFile : multipartFiles) {
            String imgUrl = imageService.save(multipartFile);
            imgUrlList.add(imgUrl);
        }

        return imgUrlList;
    }

    /**
     * 이미지 파일 1개 경로 변환
     */
    public String parseFileInfo(MultipartFile multipartFile) {
        String imgUrl = imageService.save(multipartFile);

        return imgUrl;
    }
}
