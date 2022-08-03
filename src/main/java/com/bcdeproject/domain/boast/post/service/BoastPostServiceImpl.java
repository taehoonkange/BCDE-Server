package com.bcdeproject.domain.boast.post.service;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import com.bcdeproject.domain.boast.imgurl.repository.BoastImgUrlRepository;
import com.bcdeproject.domain.boast.like.BoastLike;
import com.bcdeproject.domain.boast.like.exception.BoastLikeException;
import com.bcdeproject.domain.boast.like.exception.BoastLikeExceptionType;
import com.bcdeproject.domain.boast.like.repository.BoastLikeRepository;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.boast.post.dto.*;
import com.bcdeproject.domain.member.Member;
import com.bcdeproject.global.condition.BoastPostSearchCondition;
import com.bcdeproject.domain.boast.post.exception.BoastPostException;
import com.bcdeproject.domain.boast.post.exception.BoastPostExceptionType;
import com.bcdeproject.domain.boast.post.repository.BoastPostRepository;
import com.bcdeproject.domain.member.exception.MemberException;
import com.bcdeproject.domain.member.exception.MemberExceptionType;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.s3.service.S3UploaderService;
import com.bcdeproject.global.util.security.SecurityUtil;
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

    private final BoastPostRepository boastPostRepository;
    private final MemberRepository memberRepository;
    private final BoastImgUrlRepository boastImgUrlRepository;
    private final S3UploaderService s3UploaderService;
    private final BoastLikeRepository boastLikeRepository;


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
        boastPostRepository.save(post);
    }


    /**
     * 게시글 title, content, 이미지, 해시태그 수정
     */
    @Override
    public void update(Long id, BoastPostUpdateDto postUpdateDto, List<MultipartFile> updateImg) throws Exception{

        BoastPost post = boastPostRepository.findById(id).orElseThrow(() ->
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

                boastPostRepository.save(post);
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

        BoastPost post = boastPostRepository.findById(id).orElseThrow(() ->
                new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND));

        checkAuthority(post, BoastPostExceptionType.NOT_AUTHORITY_DELETE_POST);

        // 파일(S3)과 경로(DB) 둘 다 삭제
        if(post.getBoastImgUrlList() != null){
            List<String> imgUrls = post.getBoastImgUrlList().stream()
                    .map(boastImgUrl -> boastImgUrl.getImgUrl()).collect(Collectors.toList());
            s3UploaderService.deleteOriginalFileList(imgUrls);
        }
        // 경로 삭제
        boastPostRepository.delete(post);
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
        return new BoastPostInfoDto(boastPostRepository.findWithWriterById(id)
                .orElseThrow(() -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND)));
    }

    // TODO: HashTag도 검색 조건에 추가
    @Override
    public BoastPostSearchPagingDto searchPostList(BoastPostSearchCondition postSearchCondition) {
        Member loginMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        List<BoastHashTag> boastHashTagList = boastPostRepository.searchByHashTag(postSearchCondition);
        if(boastHashTagList.isEmpty()) {
            throw new BoastPostException(BoastPostExceptionType.SEARCH_HASHTAG_NOT_FOUND);
        } else {
            List<BriefBoastPostSearchInfoDto> briefBoastPostGetInfoDtoList = boastHashTagList.stream()
                    .map(boastHashTag -> {
                        Long findPostId = boastHashTag.getPost().getId();
                        BoastPost findBoastPost = boastPostRepository.findById(findPostId).orElseThrow(
                                () -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND));
                        // 좋아요 테이블에서 member, post로 조회 시 행이 있는 경우는 좋아요가 눌린 경우!
                        // isPresent()로 있으면 true, 없으면 false 반환
                        boolean isLike = boastLikeRepository.findByMemberAndPost(loginMember, findBoastPost).isPresent();
                        return new BriefBoastPostSearchInfoDto(findBoastPost, isLike);
                    }).collect(Collectors.toList());

            return new BoastPostSearchPagingDto(briefBoastPostGetInfoDtoList);
        }
    }

    @Override
    public BoastPostGetPagingDto getRecentPostList() {
        Member loginMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        List<BriefBoastPostGetInfoDto> briefBoastPostGetInfoDtoList = boastPostRepository.getRecentBoastPost(loginMember).stream()
                .map(boastPost -> {
                    // 좋아요 테이블에서 member, post로 조회 시 행이 있는 경우는 좋아요가 눌린 경우!
                    // isPresent()로 있으면 true, 없으면 false 반환
                    boolean isLike = boastLikeRepository.findByMemberAndPost(loginMember, boastPost).isPresent();
                    return new BriefBoastPostGetInfoDto(boastPost, isLike);
                }).collect(Collectors.toList());

        return new BoastPostGetPagingDto(briefBoastPostGetInfoDtoList);
    }

    /**
     * 좋아요 추가 로직 : 좋아요 안 눌린 상태로 요청 시
     */
    @Override
    public void addLike(Long boastPostId) {
        Member loginMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        BoastPost targetBoastPost = boastPostRepository.findById(boastPostId)
                .orElseThrow(() -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND));

        // post 테이블의 likeCount +1
        targetBoastPost.addLike();

        // 만약, 좋아요가 눌러진 상태에서(좋아요 테이블에 해당 유저, 포스트가 있는 상태) addLiked API를 호출하면 예외 발생
        boastLikeRepository.findByMemberAndPost(loginMember, targetBoastPost).ifPresent(
                none -> { throw new BoastLikeException(BoastLikeExceptionType.ALREADY_EXIST_LIKE);
                });

        // 좋아요 추가(좋아요 테이블에 해당 유저, 포스트 추가)
        boastLikeRepository.save(BoastLike.builder()
                        .post(targetBoastPost)
                        .member(loginMember)
                        .isLike(true)
                .build());
    }

    /**
     * 좋아요 삭제 로직 : 좋아요 눌린 상태로 요청 시
     */
    @Override
    public void deleteLike(Long boastPostId) {
        Member loginMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        BoastPost targetBoastPost = boastPostRepository.findById(boastPostId)
                .orElseThrow(() -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND));

        // 만약, 좋아요가 안 눌러진 상태에서(좋아요 테이블에 해당 유저, 포스트가 없는 상태) deleteLiked API를 호출하면 예외 발생
        boastLikeRepository.findByMemberAndPost(loginMember, targetBoastPost).orElseThrow(
                () -> new BoastLikeException(BoastLikeExceptionType.NOT_FOUND_LIKE));

        // 좋아요 삭제(자랑 게시물 테이블에서 count -1, 좋아요 테이블에 해당 유저, 포스트 삭제)
        targetBoastPost.deleteLike();
        boastLikeRepository.deleteByPost_IdAndMember_Id(boastPostId, loginMember.getId());
    }

}
