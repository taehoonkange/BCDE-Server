package com.bcdeproject.domain.member.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class MemberWithdrawDto {

    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String checkPassword;
}
