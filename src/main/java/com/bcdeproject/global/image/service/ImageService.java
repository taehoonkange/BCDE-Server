package com.bcdeproject.global.image.service;

import com.bcdeproject.domain.boast.imgpath.BoastImgPath;
import com.bcdeproject.global.image.exception.ImageException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {

    //저장된 파일 경로 반환
    String save(MultipartFile multipartFile) throws ImageException;

    void delete(List<BoastImgPath> imgPaths);
}
