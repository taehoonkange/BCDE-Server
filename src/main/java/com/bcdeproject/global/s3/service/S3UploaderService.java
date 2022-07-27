package com.bcdeproject.global.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3UploaderService {

    // local, development 등 현재 프로파일
    @Value("${spring.environment}")
    private String environment;

    // 파일이 저장되는 경로
    @Value("${spring.file-dir}")
    private String rootDir;
    private String fileDir;

    // S3 bucket 이름
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // S3 이미지 저장 폴더 이름
    private String dirName = "image";

    // 삭제시 요청 URL에서 제외할 URL
    private String deleteUrl = "https://bcde-bucket.s3.ap-northeast-2.amazonaws.com/";

    private final AmazonS3 amazonS3Client;

    /**
     * 서버가 시작할 때 프로파일에 맞는 파일 경로를 설정해줌
     */
    @PostConstruct
    private void init(){
        if(environment.equals("local")){
            this.fileDir = System.getProperty("user.dir") + this.rootDir;
        }
        else if(environment.equals("development")){
            this.fileDir = this.rootDir;
        }

    }

    public String upload(MultipartFile multipartFile) throws IOException {
        File uploadFile = convert(multipartFile)  // 파일 변환할 수 없으면 에러
                .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

        return upload(uploadFile);
    }

    // S3로 파일 업로드하기
    private String upload(File uploadFile) {
        String fileName = dirName + "/" + UUID.randomUUID() + uploadFile.getName();   // S3에 저장된 파일 이름
        String uploadImageUrl = putS3(uploadFile, fileName); // s3로 업로드
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    public List<String> uploadList(List<MultipartFile> multipartFileList) throws IOException {

        // 반환을 할 이미지 리스트
        List<String> imgUrlList = new ArrayList<>();

        // 이미지가 빈 것이 들어오면 빈 것을 반환
        if (multipartFileList.isEmpty()) {
            return imgUrlList;
        }

        for (MultipartFile multipartFile : multipartFileList) {
            File uploadFile = convert(multipartFile)
                    .orElseThrow(() -> new IllegalArgumentException("error: MultipartFile -> File convert fail"));

            String imgUrl = upload(uploadFile);
            imgUrlList.add(imgUrl);
        }

        return imgUrlList;
    }

    // S3로 업로드
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // 로컬에 저장된 이미지 지우기
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    // DB에 저장된 URL이 fileName으로 들어옴 : https://bcde-bucket.s3.ap-northeast-2.amazonaws.com/image/~~
    // deleteObject의 delete Key는 https://bcde-bucket.s3.ap-northeast-2.amazonaws.com/을 제외한 image/~~ 이므로 URL 수정
    public void deleteOriginalFile(String fileName) {
        String final_fileName = fileName.toString().replace(deleteUrl, "");
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, final_fileName));
    }

    /**
     * @param multipartFile
     * 로컬에 파일 저장하기
     */
    private Optional<File> convert(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return Optional.empty();
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);

        //파일 업로드
        File file = new File(fileDir+storeFileName);
        multipartFile.transferTo(file);

        return Optional.of(file);
    }

    /**
     * @description 파일 이름이 이미 업로드된 파일들과 겹치지 않게 UUID를 사용한다.
     * @param originalFilename 원본 파일 이름
     * @return 파일 이름
     */
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    /**
     * @description 사용자가 업로드한 파일에서 확장자를 추출한다.
     *
     * @param originalFilename 원본 파일 이름
     * @return 파일 확장자
     */
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

}
