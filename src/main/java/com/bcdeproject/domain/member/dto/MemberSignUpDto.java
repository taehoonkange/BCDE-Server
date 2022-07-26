package com.bcdeproject.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "회원 가입 항목")
public class MemberSignUpDto {

    @ApiModelProperty(value = "회원 ID(ID는 5~20자 내외로 입력)", required = true)
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 5, max = 20, message = "아이디는 5~20자 내외로 입력해주세요.")
    private String username;

    @ApiModelProperty(value = "회원 비밀번호(비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함)", required = true)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String password;

    @ApiModelProperty(value = "회원 닉네임(닉네임은 2자 이상 입력)", required = true)
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(min = 2, message = "닉네임은 2자 이상으로 입력해주세요.")
    private String nickName;
}