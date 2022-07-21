package com.bcdeproject.domain.member.dto;

import com.bcdeproject.domain.member.Member;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MemberInfoDto {

    private String username;
    private String nickName;
    private String profileImgPath;

    @Builder
    public MemberInfoDto(Member member) {
        this.username = member.getUsername();
        this.nickName = member.getNickName();
        this.profileImgPath = member.getProfileImgPath();
    }
}