package com.bcdeproject.domain.member.service;

import com.bcdeproject.domain.member.dto.MemberInfoDto;
import com.bcdeproject.domain.member.dto.MemberSignUpDto;
import com.bcdeproject.domain.member.dto.MemberUpdateDto;
import org.springframework.web.multipart.MultipartFile;

public interface MemberService {

    /**
     * 회원가입
     * 정보수정
     * 회원탈퇴
     * 정보조회
     */

    void signUp(MemberSignUpDto memberSignUpDto, MultipartFile profileImg) throws Exception;

    void update(MemberUpdateDto memberUpdateDto) throws Exception;

    void updatePassword(String checkPassword, String toBePassword) throws Exception;

    void withdraw(String checkPassword) throws Exception;

    MemberInfoDto getInfo(Long memberId) throws Exception;

    MemberInfoDto getMyInfo() throws Exception;


}