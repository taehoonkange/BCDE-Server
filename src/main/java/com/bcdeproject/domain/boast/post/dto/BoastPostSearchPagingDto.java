package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class BoastPostSearchPagingDto {

    private List<BriefBoastPostSearchInfoDto> searchBoastPostList = new ArrayList<>();


    public BoastPostSearchPagingDto(List<BriefBoastPostSearchInfoDto> briefBoastPostSearchInfoDtoList) {
        this.searchBoastPostList = briefBoastPostSearchInfoDtoList;
    }
}