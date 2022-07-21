package com.bcdeproject.domain.boast.comment.service;

import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentSaveDto;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentUpdateDto;
import com.bcdeproject.domain.boast.comment.exception.BoastCommentException;

import java.util.List;

public interface BoastCommentService {
    void save(Long postId , BoastCommentSaveDto commentSaveDto);
    void saveReComment(Long postId, Long parentId, BoastCommentSaveDto commentSaveDto);

    void update(Long id, BoastCommentUpdateDto commentUpdateDto);

    void remove(Long id) throws BoastCommentException;
}
