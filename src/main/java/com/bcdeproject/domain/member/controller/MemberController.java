package com.bcdeproject.domain.member.controller;

import com.bcdeproject.domain.member.dto.*;
import com.bcdeproject.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입
     * TODO : MultipartFile로 프로필 사진 이미지 받기
     */
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public void signUp(@Valid @RequestPart MemberSignUpDto memberSignUpDto,
                       @RequestPart (required = false) MultipartFile profileImg) throws Exception {
        memberService.signUp(memberSignUpDto, profileImg);
    }

    /**
     * 회원정보수정
     */
    @PatchMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void updateBasicInfo(@Valid @RequestPart MemberUpdateDto memberUpdateDto,
                                @RequestPart(required = false) MultipartFile updateProfileImg) throws Exception {
        memberService.update(memberUpdateDto, updateProfileImg);
    }

    /**
     * 비밀번호 수정
     */
    @PatchMapping("/member/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.getCheckPassword(),updatePasswordDto.getToBePassword());
    }


    /**
     * 회원탈퇴
     */
    @DeleteMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.getCheckPassword());
    }


    /**
     * 회원정보조회
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity getInfo(@Valid @PathVariable("memberId") Long memberId) throws Exception {
        MemberInfoDto info = memberService.getInfo(memberId);
        return new ResponseEntity(info, HttpStatus.OK);
    }

    /**
     * 내정보조회
     */
    @GetMapping("/member")
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {

        MemberInfoDto info = memberService.getMyInfo();
        return new ResponseEntity(info, HttpStatus.OK);
    }

}