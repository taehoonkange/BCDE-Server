package com.bcdeproject.domain.boast.post.service;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.hashtag.dto.BoastHashTagDto;
import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import com.bcdeproject.domain.boast.imgurl.repository.BoastImgUrlRepository;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.global.condition.BoastPostSearchCondition;
import com.bcdeproject.domain.boast.post.dto.BoastPostInfoDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostPagingDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostSaveDto;
import com.bcdeproject.domain.boast.post.dto.BoastPostUpdateDto;
import com.bcdeproject.domain.boast.post.exception.BoastPostException;
import com.bcdeproject.domain.boast.post.exception.BoastPostExceptionType;
import com.bcdeproject.domain.boast.post.repository.BoastPostRepository;
import com.bcdeproject.domain.member.exception.MemberException;
import com.bcdeproject.domain.member.exception.MemberExceptionType;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.s3.service.S3UploaderService;
import com.bcdeproject.global.util.security.SecurityUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoastPostServiceImpl implements BoastPostService{

    private final BoastPostRepository postRepository;
    private final MemberRepository memberRepository;
    private final BoastImgUrlRepository boastImgUrlRepository;
    private final S3UploaderService s3UploaderService;


    @Override
    public void save(BoastPostSaveDto postSaveDto, List<MultipartFile> uploadImg) throws Exception {
        BoastPost post = BoastPost.builder()
                .title(postSaveDto.getTitle())
                .content(postSaveDto.getContent())
                .build();


        // post 작성자 : 로그인한 유저 객체 저장
        post.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        // post(게시글)에 이미지 리스트 저장
        if(uploadImg == null) log.info("이미지 X");
        else {
            imgListSave(post, uploadImg);
        }

        // post(게시글)에 해시태그 리스트 저장
        List<String> hashTags = postSaveDto.getHashTag();
        if(hashTags == null) log.info("해시태그 X");
        else {
            log.info("요청 해시태그 name : {}", postSaveDto.getHashTag().toString());
            hashTagListSave(post, hashTags);
        }

        // 이미지와 해시태그 리스트가 추가된 post DB에 저장
        postRepository.save(post);
    }


    /**
     * 게시글 title, content, 이미지, 해시태그 수정
     */
    @Override
    public void update(Long id, BoastPostUpdateDto postUpdateDto, List<MultipartFile> updateImg) throws Exception{

        BoastPost post = postRepository.findById(id).orElseThrow(() ->
                new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND));

        checkAuthority(post, BoastPostExceptionType.NOT_AUTHORITY_UPDATE_POST);

        // 업데이트 요청에 타이틀이 있을 때 업데이트, 없으면 예외 반환(타이틀은 필수 -> 업데이트 하지 않으면 현재 내용 담아서 요청)
        if(postUpdateDto.getTitle() != null) {
            post.updateTitle(postUpdateDto.getTitle());
        } else {
            throw new BoastPostException(BoastPostExceptionType.UPDATE_POST_TITLE_NOT_FOUND);
        }

        // 업데이트 요청에 내용이 있을 때 업데이트, 없으면 예외 반환(내용은 필수 -> 업데이트 하지 않으면 현재 내용 담아서 요청)
        if(postUpdateDto.getContent() != null) {
            post.updateContent(postUpdateDto.getContent());
        } else {
            throw new BoastPostException(BoastPostExceptionType.UPDATE_POST_CONTENT_NOT_FOUND);
        }

        // 업데이트 요청에 해시태그가 있다면,
        if(postUpdateDto.getHashTag() != null) {
            List<String> hashTags = postUpdateDto.getHashTag();
            // 기존 post 해시태그 있다면
            if (post.getBoastHashTagList() != null) {
                // 현재 post 해시태그 모두 삭제
                post.getBoastHashTagList().clear();

                // post(게시글) 해시태그 이미지 리스트 업데이트(저장)
                hashTagListSave(post, hashTags);

                postRepository.save(post);
            }
        } else {
            throw new BoastPostException(BoastPostExceptionType.UPDATE_POST_HASHTAG_NOT_FOUND);
        }

        // 업데이트 요청에 이미지가 있다면,
        if(updateImg != null) {
            // 기존 post의 이미지가 있다면
            if(post.getBoastImgUrlList() != null){
                // 기존에 올린 파일 S3, DB에서 삭제
                imgListDelete(post);


                // post(게시글)에 업데이트 이미지 리스트 업데이트(저장)
                imgListSave(post, updateImg);
            }
        }

        log.info("게시글의 이미지 리스트 : {}", post.getBoastImgUrlList());

    }

    // 이미지 String(경로)로 변환 후 이미지마다 BoastImgUrl 객체 빌더로 생성해서 post에 addBoastImgUrl로 저장
    public void imgListSave(BoastPost post, List<MultipartFile> uploadImgs) throws Exception {
        List<String> imgUrls = s3UploaderService.uploadList(uploadImgs);
        for (String imgUrl : imgUrls) {
            BoastImgUrl boastImgUrl = BoastImgUrl.builder()
                    .imgUrl(imgUrl)
                    .post(post)
                    .writer(post.getWriter())
                    .build();
            post.addBoastImgUrl(boastImgUrl);
        }
    }

    // 해당 post id로 find하여 이미지들을 찾은 후 반복문을 돌면서 DB와 S3에서 삭제
    public void imgListDelete(BoastPost post) throws Exception {
        List<BoastImgUrl> imgUrls = boastImgUrlRepository.findByPost(post);
        for(BoastImgUrl imgUrl : imgUrls) {
            log.info("삭제할 BoastImgUrl = {}", imgUrls.get(0).getImgUrl());
            // DB에서 삭제하기 위해서는 부모 객체의 자식 List를 remove하면 자동으로 삭제된다.
            post.removeBoastImgUrl(imgUrl);
            //            boastImgUrlRepository.delete(imgUrl);
            s3UploaderService.deleteOriginalFile(imgUrl.getImgUrl());

        }

    }

    // 해시태그마다 BoastHashTag 객체 빌더로 생성해서 post에 addBoastHashTag로 저장
    public void hashTagListSave(BoastPost post, List<String> hashTags) {
        for(String hashTag: hashTags) {
            BoastHashTag boastHashTag = BoastHashTag.builder()
                    .name(hashTag)
                    .post(post)
                    .build();
            post.addBoastHashTag(boastHashTag);
        }
    }

    @Override
    public void delete(Long id) {

        BoastPost post = postRepository.findById(id).orElseThrow(() ->
                new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND));

        checkAuthority(post, BoastPostExceptionType.NOT_AUTHORITY_DELETE_POST);

        // 파일(S3)과 경로(DB) 둘 다 삭제
        if(post.getBoastImgUrlList() != null){
            List<String> imgUrls = post.getBoastImgUrlList().stream()
                    .map(boastImgUrl -> boastImgUrl.getImgUrl()).collect(Collectors.toList());
            s3UploaderService.deleteOriginalFileList(imgUrls);
        }
        // 경로 삭제
        postRepository.delete(post);
    }


    private void checkAuthority(BoastPost post, BoastPostExceptionType postExceptionType) {
        if(!post.getWriter().getUsername().equals(SecurityUtil.getLoginUsername()))
            throw new BoastPostException(postExceptionType);
    }

    /**
     * Post의 id를 통해 Post 조회
     */
    @Override
    public BoastPostInfoDto getPostInfo(Long id) {
        /**
         * Post + MEMBER 조회 -> 쿼리 1번 발생
         *
         * 댓글&대댓글 리스트 조회 -> 쿼리 1번 발생(POST ID로 찾는 것이므로, IN쿼리가 아닌 일반 where문 발생)
         * (댓글과 대댓글 모두 Comment 클래스이므로, JPA는 구분할 방법이 없어서, 당연히 CommentList에 모두 나오는것이 맞다,
         * 가지고 온 것을 가지고 우리가 구분지어주어야 한다.)
         *
         * 댓글 작성자 정보 조회 -> 배치사이즈를 이용했기때문에 쿼리 1번 혹은 N/배치사이즈 만큼 발생
         *
         *
         */
        return new BoastPostInfoDto(postRepository.findWithWriterById(id)
                .orElseThrow(() -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND)));
    }

    // TODO: HashTag도 검색 조건에 추가
    @Override
    public BoastPostPagingDto searchPostList(Pageable pageable, BoastPostSearchCondition postSearchCondition) {
        return new BoastPostPagingDto(postRepository.search(postSearchCondition, pageable));
    }


}
