package com.bcdeproject.domain.boast.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@NoArgsConstructor
public class BoastCommentUpdateDto {

    private Optional<String> content;
}
