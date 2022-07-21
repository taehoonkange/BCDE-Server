package com.bcdeproject.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberUpdateDto {

    private String nickName;

    private String profileImgPath;
}