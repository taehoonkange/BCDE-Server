package com.bcdeproject.domain.member.dto;

import com.bcdeproject.domain.member.Member;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@ApiModel(description = "회원 조회 결과 항목")
public class MemberInfoDto {

    @ApiModelProperty(value = "회원 ID", required = true)
    private String username;

    @ApiModelProperty(value = "회원 닉네임", required = true)
    private String nickName;

    @ApiModelProperty(value = "회원 프로필 사진 URL", required = true)
    private String profileImgUrl;

    @Builder
    public MemberInfoDto(Member member) {
        this.username = member.getUsername();
        this.nickName = member.getNickName();
        this.profileImgUrl = member.getProfileImgUrl();
    }
}