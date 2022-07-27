package com.bcdeproject.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Data
@ApiModel(description = "회원 탈퇴 항목")
public class MemberWithdrawDto {

    @ApiModelProperty(value = "현재 회원 비밀번호", required = true)
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String checkPassword;

}
