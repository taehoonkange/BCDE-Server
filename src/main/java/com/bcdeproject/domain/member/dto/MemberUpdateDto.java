package com.bcdeproject.domain.member.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "회원 정보 수정 항목")
public class MemberUpdateDto {

    @ApiModelProperty(value = "회원 닉네임", required = true)
    private String nickName;

}