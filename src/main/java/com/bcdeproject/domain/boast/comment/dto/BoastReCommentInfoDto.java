package com.bcdeproject.domain.boast.comment.dto;

import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.member.dto.MemberInfoDto;
import lombok.Data;

@Data
public class BoastReCommentInfoDto {

    private final static String DEFAULT_DELETE_MESSAGE = "삭제된 댓글입니다";

    private Long postId;
    private Long parentId;


    private Long reCommentId;
    private String content;
    private boolean isRemovedReComment;


    private MemberInfoDto reCommentWriterDto;

    public BoastReCommentInfoDto(BoastComment reComment) {
        this.postId = reComment.getPost().getId();
        this.parentId = reComment.getParent().getId();
        this.reCommentId = reComment.getId();
        this.content = reComment.getContent();

        if(reComment.isRemoved()){
            this.content = DEFAULT_DELETE_MESSAGE;
        }

        this.isRemovedReComment = reComment.isRemoved();
        this.reCommentWriterDto = new MemberInfoDto(reComment.getWriter());
    }
}
