package com.bcdeproject.domain.member.service;

import com.bcdeproject.domain.member.Member;
import com.bcdeproject.domain.member.dto.MemberInfoDto;
import com.bcdeproject.domain.member.dto.MemberSignUpDto;
import com.bcdeproject.domain.member.dto.MemberUpdateDto;
import com.bcdeproject.domain.member.exception.MemberException;
import com.bcdeproject.domain.member.exception.MemberExceptionType;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 로직
     * 서비스 단에서 DTO를 엔티티로 변환
     * 변환 후 USER 권한 설정, 이후 중복된 아이디가 있는지 체크
     */
    @Override
    public void signUp(MemberSignUpDto memberSignUpDto) throws Exception {
        Member member = Member.builder()
                .username(memberSignUpDto.getUsername())
                .password(memberSignUpDto.getPassword())
                .nickName(memberSignUpDto.getNickName())
                .profileImgUrl(memberSignUpDto.getProfileImgUrl())
                .build();

        member.addUserAuthority();
        member.encodePassword(passwordEncoder);


        if(memberRepository.findByUsername(memberSignUpDto.getUsername()).isPresent()){
            throw new MemberException(MemberExceptionType.ALREADY_EXIST_USERNAME);
        }

        memberRepository.save(member);
    }

    /**
     * 회원 정보 수정 로직 (닉네임, 프로필 사진)
     * 요청 시 닉네임, 프로필 사진 하나만 보내도 OK
     * 닉네임, 프로필 사진 둘 중 아무것도 안 보냈을 때 예외 발생
     */
    @Override
    public void update(MemberUpdateDto memberUpdateDto) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(memberUpdateDto.getNickName() != null) member.updateNickName(memberUpdateDto.getNickName());
        if(memberUpdateDto.getProfileImgUrl() != null) member.updateProfileImgUrl(memberUpdateDto.getProfileImgUrl());

        if(memberUpdateDto.getNickName() == null && memberUpdateDto.getProfileImgUrl() == null) {
            throw new MemberException(MemberExceptionType.MEMBER_UPDATE_INFO_NOT_FOUND);
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
     */
    @Override
    public void withdraw(String checkPassword) throws Exception {
        Member member = memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));

        if(!member.matchPassword(passwordEncoder, checkPassword) ) {
            throw new MemberException(MemberExceptionType.WRONG_PASSWORD);
        }

        memberRepository.delete(member);
    }


    /**
     * ID 회원 정보 조회 로직
     * id를 받아서 id에 해당하는 회원의 정보를 조회 (여기서 id는 유저 아이디가 아니라 -> DB의 seq, idx)
     */
    @Override
    public MemberInfoDto getInfo(Long id) throws Exception {
        Member findMember = memberRepository.findById(id).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER));
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
}