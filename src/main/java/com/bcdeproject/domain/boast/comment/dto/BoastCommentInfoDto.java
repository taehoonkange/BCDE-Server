package com.bcdeproject.domain.boast.comment.dto;

import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.member.dto.MemberInfoDto;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class BoastCommentInfoDto {

    private final static String DEFAULT_DELETE_MESSAGE = "삭제된 댓글입니다";

    private Long postId;//댓글이 달린 POST의 ID


    private Long commentId;//해당 댓글의 ID
    private String content;//내용 (삭제되었다면 "삭제된 댓글입니다 출력")
    private boolean isRemovedComment;//삭제되었는지?


    private MemberInfoDto commentWriterDto;//댓글 작성자에 대한 정보

    private List<BoastReCommentInfoDto> reCommentDtoList;//대댓글에 대한 정보들


    /**
     * 삭제되었을 경우 삭제된 댓글입니다 출력
     */

    public BoastCommentInfoDto(BoastComment comment, List<BoastComment> reCommentList) {

        this.postId = comment.getPost().getId();
        this.commentId = comment.getId();


        this.content = comment.getContent();

        if(comment.isRemoved()){
            this.content = DEFAULT_DELETE_MESSAGE;
        }

        this.isRemovedComment = comment.isRemoved();



        this.commentWriterDto = new MemberInfoDto(comment.getWriter());

        this.reCommentDtoList = reCommentList.stream().map(BoastReCommentInfoDto::new).collect(Collectors.toList());

    }
}