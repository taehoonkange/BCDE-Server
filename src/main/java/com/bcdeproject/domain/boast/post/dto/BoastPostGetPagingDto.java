package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.post.BoastPost;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoastPostGetPagingDto {

    private List<BriefBoastPostGetInfoDto> boastPostList = new ArrayList<>();


    public BoastPostGetPagingDto(List<BriefBoastPostGetInfoDto> briefBoastPostGetInfoDtoList) {
        this.boastPostList = briefBoastPostGetInfoDtoList;
    }
}