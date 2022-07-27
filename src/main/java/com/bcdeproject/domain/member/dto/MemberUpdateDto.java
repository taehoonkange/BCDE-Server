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

    /**
     * originalFileName Example
     * image/4f78491e-5442-4655-8d8b-f053655d37a170416ed9-0827-46e2-a8c4-1644f485c1d3.jpg
     */
    @ApiModelProperty(value = "기존 회원 프로필 사진 Name")
    private Optional<String> originalFileName;
}