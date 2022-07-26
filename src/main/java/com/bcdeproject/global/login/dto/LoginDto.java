package com.bcdeproject.global.login.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@ApiModel(description = "로그인 항목")
public class LoginDto {

    @ApiModelProperty(value = "로그인할 회원 ID", required = true)
    @NotBlank(message = "로그인할 회원 ID를 입력하세요.")
    private String username;

    @ApiModelProperty(value = "로그인할 회원 비밀번호", required = true)
    @NotBlank(message = "로그인할 회원 비밀번호를 입력하세요.")
    private String password;
}
