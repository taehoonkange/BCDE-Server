package com.bcdeproject.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@ApiModel(description = "회원 비밀번호 수정 항목")
public class UpdatePasswordDto {

    @ApiModelProperty(value = "현재 회원 비밀번호", required = true)
    @NotBlank(message = "현재 비밀번호를 입력해주세요.")
    private String checkPassword;

    @ApiModelProperty(value = "변경할 회원 비밀번호(비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함)", required = true)
    @NotBlank(message = "바꿀 비밀번호를 입력해주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,30}$",
            message = "비밀번호는 8~30 자리이면서 1개 이상의 알파벳, 숫자, 특수문자를 포함해야합니다.")
    private String toBePassword;
}
