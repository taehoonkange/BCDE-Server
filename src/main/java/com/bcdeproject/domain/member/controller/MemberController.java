package com.bcdeproject.domain.member.controller;

import com.bcdeproject.domain.member.dto.*;
import com.bcdeproject.domain.member.service.MemberService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @Operation(summary = "회원가입 API", description = "회원가입 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public String signUp(@Valid @RequestPart MemberSignUpDto memberSignUpDto,
                       @RequestPart (required = false) @ApiParam(value = "회원 프로필 사진") MultipartFile profileImg) throws Exception {
        memberService.signUp(memberSignUpDto, profileImg);
        return "회원가입 완료";
    }

    /**
     * 회원정보수정
     */
    @Operation(summary = "회원 정보 수정 API", description = "회원 정보 수정 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 회원입니다"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PatchMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public void updateBasicInfo(@Valid @RequestPart MemberUpdateDto memberUpdateDto,
                                @RequestPart(required = false) @ApiParam(value = "프로필 사진 수정 이미지") MultipartFile updateProfileImg) throws Exception {
        memberService.update(memberUpdateDto, updateProfileImg);
    }

    /**
     * 비밀번호 수정
     */
    @Operation(summary = "비밀번호 수정 API", description = "비밀번호 수정 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 회원입니다"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @PatchMapping("/member/password")
    @ResponseStatus(HttpStatus.OK)
    public String updatePassword(@Valid @RequestBody UpdatePasswordDto updatePasswordDto) throws Exception {
        memberService.updatePassword(updatePasswordDto.getCheckPassword(),updatePasswordDto.getToBePassword());

        return "비밀번호 수정 완료"
    }


    /**
     * 회원탈퇴
     */
    @Operation(summary = "회원 탈퇴 API", description = "회원 탈퇴 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 회원입니다"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @DeleteMapping("/member")
    @ResponseStatus(HttpStatus.OK)
    public String withdraw(@Valid @RequestBody MemberWithdrawDto memberWithdrawDto) throws Exception {
        memberService.withdraw(memberWithdrawDto.getCheckPassword());
        return "회원 탈퇴 완료";
    }


    /**
     * 회원정보조회
     */
    @Operation(summary = "회원 정보 조회 API", description = "회원 정보 조회 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 회원입니다"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @GetMapping("/member/{memberId}")
    public ResponseEntity getInfo(@Valid @PathVariable("memberId") Long memberId) throws Exception {
        MemberInfoDto info = memberService.getInfo(memberId);
        return new ResponseEntity(info, HttpStatus.OK);
    }

    /**
     * 내정보조회
     */
    @Operation(summary = "내 정보 조회 API", description = "내 정보 조회 API Example")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청입니다."),
            @ApiResponse(responseCode = "403", description = "인증되지 않은 회원입니다"),
            @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500", description = "서버 에러"),
    })
    @GetMapping("/member")
    public ResponseEntity getMyInfo(HttpServletResponse response) throws Exception {

        MemberInfoDto info = memberService.getMyInfo();
        return new ResponseEntity(info, HttpStatus.OK);
    }

}