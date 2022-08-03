package com.bcdeproject.domain.member.service;

import com.bcdeproject.domain.boast.like.repository.BoastLikeRepository;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.boast.post.dto.BoastPostGetPagingDto;
import com.bcdeproject.domain.boast.post.dto.BriefBoastPostGetInfoDto;
import com.bcdeproject.domain.boast.post.repository.BoastPostRepository;
import com.bcdeproject.domain.member.Member;
import com.bcdeproject.domain.member.dto.MemberInfoDto;
import com.bcdeproject.domain.member.dto.MemberSignUpDto;
import com.bcdeproject.domain.member.dto.MemberUpdateDto;
import com.bcdeproject.domain.member.exception.MemberException;
import com.bcdeproject.domain.member.exception.MemberExceptionType;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.s3.service.S3UploaderService;
import com.bcdeproject.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final S3UploaderService s3UploaderService;
    private final BoastPostRepository boastPostRepository;
    private final BoastLikeRepository boastLikeRepository;

    /**
     * 회원가입 로직
     * 서비스 단에서 DTO를 엔티티로 변환
     * 변환 후 USER 권한 설정, 이후 중복된 아이디가 있는지 체크
     */
    @Override
    public void signUp(MemberSignUpDto memberSignUpDto, MultipartFile profileImg) throws Exception {

        // ID가 이미 존재한다면 회원 중복 예외 발생
        if(memberRepository.findByUsername(memberSignUpDto.getUsername()).isPresent()){
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }

        // profileImg가 들어온다면, member 객체 빌더로 생성 시 profileImg도 생성하여 DB에 저장
        if(profileImg != null) {
            String profileImgUrl = s3UploaderService.upload(profileImg);
            log.info("프로필 사진 기존 파일 이름 : {}", profileImg.getOriginalFilename());
            log.info("프로필 사진 이미지 url : {}", profileImgUrl);


            Member member = Member.builder()
                    .username(memberSignUpDto.getUsername())
                    .password(memberSignUpDto.getPassword())
                    .nickName(memberSignUpDto.getNickName())
                    .profileImgUrl(profileImgUrl)
                    .build();

            member.addUserAuthority();
            member.encodePassword(passwordEncoder);

            memberRepository.save(member);

        // profileImg가 들어오지 않는다면, member 객체 빌더로 생성 시 profileImg를 제외하고 생성하여 DB에 저장
        } else {
            Member member = Member.builder()
                    .username(memberSignUpDto.getUsername())
                    .password(memberSignUpDto.getPassword())
                    .nickName(memberSignUpDto.getNickName())
                    .build();

            member.addUserAuthority();
            member.encodePassword(passwordEncoder);

            memberRepository.save(member);
        }

    }

    /**
     * 회원 정보 수정 로직 (닉네임, 프로필 사진)
     * 요청 시 닉네임, 프로필 사진 하나만 보내도 OK
     * 닉네임, 프로필 사진 둘 중 아무것도 안 보냈을 때 예외 발생
     * TODO : 추후에 기존 프로필 사진이 기본 프로필 사진일 때는 파일 삭제 안하도록 구현
     */
    @Override
    public void update(MemberUpdateDto memberUpdateDto, MultipartFile updateProfileImg) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

            // 닉네임이 정상적으로 요청됐다면, 업데이트
            if (memberUpdateDto.getNickName() != null) member.updateNickName(memberUpdateDto.getNickName());

            //  닉네임, 이미지 둘 다 요청 X인 경우
            if (memberUpdateDto.getNickName() == null && updateProfileImg == null) { // dto.getNickName() == null : dtd에서 nickName이 없을 경우
                throw new MemberException(MemberExceptionType.MEMBER_UPDATE_INFO_NOT_FOUND);
            }

        // 업데이트 요청에 프로필 사진 이미지가 있다면, 업데이트
        if (updateProfileImg != null) {
            String updateProfileImgUrl = s3UploaderService.upload(updateProfileImg);
            // 기존 member의 이미지가 있다면
            if (member.getProfileImgUrl() != null) {
                s3UploaderService.deleteOriginalFile(member.getProfileImgUrl()); // 기존에 올린 파일 저장소에서 지우기
                member.updateProfileImgUrl(updateProfileImgUrl); // DB에 업데이트 이미지로 수정
            }
        }
    }

    /**
     * 비밀번호 변경 로직
     * 비밀번호 변경 시 현재 비밀번호를 체크하여 보안 강화
     * 요청 시 현재 비밀번호, 바꿀 비밀번호를 받아서 현재 비밀번호로 체크 후 맞다면 바꿀 비밀번호로 변경
     */
    @Override
    public void updatePassword(String checkPassword, String toBePassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        member.updatePassword(passwordEncoder, toBePassword);
    }


    /**
     * 회원 탈퇴 로직
     * 회원 탈퇴 시 현재 비밀번호를 체크하여 일치하면 탈퇴 진행
     * TODO : 추후에 기본 프로필 사진일 때는 파일 삭제 안하도록 구현
     */
    @Override
    public void withdraw(String checkPassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        } else {
            s3UploaderService.deleteOriginalFile(member.getProfileImgUrl());
            memberRepository.delete(member);
        }
    }



    /**
     * ID 회원 정보 조회 로직
     * id를 받아서 id에 해당하는 회원의 정보를 조회 (여기서 id는 유저 아이디가 아니라 -> DB의 seq, idx)
     */
    @Override
    public MemberInfoDto getInfo(Long memberId) throws Exception {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberInfoDto(findMember);
    }

    /**
     * 내 정보 조회 로직
     * SecurityUtil.getLoginUsername()을 사용하여 현재 로그인한 유저의 정보 조회
     */
    @Override
    public MemberInfoDto getMyInfo() throws Exception {
        Member findMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
        return new MemberInfoDto(findMember);
    }


    /**
     * 내 자랑 게시물 조회 로직
     */
    @Override
    public BoastPostGetPagingDto getMyBoastPostList() {
        Member loginMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        List<BriefBoastPostGetInfoDto> briefBoastPostGetInfoDtoList = boastPostRepository.findAllByWriterId(loginMember.getId()).stream()
                .map(boastPost -> {
                    int boastPostLikeCount = boastPost.getLikeCount();
                    // 좋아요 테이블에서 member, post로 조회 시 행이 있는 경우는 좋아요가 눌린 경우!
                    // isPresent()로 있으면 true, 없으면 false 반환
                    boolean isLike = boastLikeRepository.findByMemberAndPost(loginMember, boastPost).isPresent();
                    return new BriefBoastPostGetInfoDto(boastPost, boastPostLikeCount, isLike);
                }).collect(Collectors.toList());

        return new BoastPostGetPagingDto(briefBoastPostGetInfoDtoList);
    }

    /**
     * 내가 좋아요 누른 자랑 게시물 조회
     */
    @Override
    public BoastPostGetPagingDto getMyLikeBoastPostList() {
        Member loginMember = memberRepository.findByUsername(SecurityUtil.getLoginUsername())
                .orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        List<BriefBoastPostGetInfoDto> briefBoastPostGetInfoDtoList = boastLikeRepository.findAllByMemberId(loginMember.getId()).stream()
                .map(boastLike -> {
                    BoastPost findBoastPost = boastLike.getPost();
                    int boastPostLikeCount = findBoastPost.getLikeCount();
                    // 좋아요 테이블에서 member, post로 조회 시 행이 있는 경우는 좋아요가 눌린 경우!
                    // isPresent()로 있으면 true, 없으면 false 반환
                    boolean isLike = boastLikeRepository.findByMemberAndPost(loginMember, findBoastPost).isPresent();
                    return new BriefBoastPostGetInfoDto(findBoastPost, boastPostLikeCount, isLike);
                }).collect(Collectors.toList());

        return new BoastPostGetPagingDto(briefBoastPostGetInfoDtoList);
    }

}