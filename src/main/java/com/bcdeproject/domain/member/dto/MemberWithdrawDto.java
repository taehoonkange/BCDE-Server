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

    /**
     * originalFileName Example
     * image/4f78491e-5442-4655-8d8b-f053655d37a170416ed9-0827-46e2-a8c4-1644f485c1d3.jpg
     */
    @ApiModelProperty(value = "기존 회원 프로필 사진 Name", required = true)
    @NotBlank(message = "기존 회원의 프로필 사진 Name을 입력해주세요.")
    private Optional<String> originalFileName;
}
