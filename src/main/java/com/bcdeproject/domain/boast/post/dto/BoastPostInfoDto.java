package com.bcdeproject.domain.boast.post.dto;

import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentInfoDto;
import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.dto.MemberInfoDto;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class BoastPostInfoDto {

    private Long postId; //POST의 ID
    private String title;//제목
    private String content;//내용
    private List<BoastImgUrl> imgUrl;//업로드 파일 경로 리스트
    private List<BoastHashTag> hashTag;

    private MemberInfoDto writerDto;//작성자에 대한 정보


    private List<BoastCommentInfoDto> commentInfoDtoList;//댓글 정보들


    public BoastPostInfoDto(BoastPost post) {

        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imgUrl = post.getBoastImgUrlList();
        this.hashTag = post.getBoastHashTagList();


        this.writerDto = new MemberInfoDto(post.getWriter());




        /**
         * 댓글과 대댓글을 그룹짓기
         * post.getCommentList()는 댓글과 대댓글이 모두 조회된다.
         *
         *  (1) post의 Comment 리스트를 가져옵니다.
         *  이때 CommentList는 댓글과 대댓글이 모두 섞여있는 상태
         *  그 이유는 Comment와 Recomment는 단지 parent가 있는지 없는지로만 구분지어지므로, JPA는 댓글과 대댓글을 구분할 방법이 없다.
         *  따라서 CommentList를 통해 댓글과 대댓글을 모두 가져오게 되는 것입니다.
         *  (추가로 이때 배치 사이즈를 100으로 설정해 주었기 때문에, 쿼리는 1번 혹은 N/100만큼 발생하게 된다.)
         *
         *  (2) Comment의 parent가 null이 아닌, 즉 댓글이 아닌 대댓글인 것들만 가져온다.
         *
         *  (3) 필터링 된 것들은 모두 대댓글이고, 대댓글의 Parent(댓글)를 통해 그룹핑한다.
         *  이렇게 되면 Map에는 <댓글, List<해당 댓글에 달린 대댓글>>의 형식으로 그룹핑된다.
         *
         *  (4) 그룹지은 것들 중 keySet , 즉 댓글들을 가지고 온다.
         *
         *  (5) 댓글들을 CommentInfoDto로 변환시켜준다.
         *  이때 CommentInfoDto의 생성자로 댓글과 해당 댓글에 달린 대댓글들을 인자로 넣어준다.
         */


        Map<BoastComment, List<BoastComment>> commentListMap = post.getBoastCommentList().stream() // (1)

                // 대댓글인 경우만 필터로 걸러낸다.
                .filter(boastComment -> boastComment.getParent() != null) // (2)

                // Parent(댓글) id로 그룹짓기
                .collect(Collectors.groupingBy(BoastComment::getParent)); // (3)

        /**
         * 댓글과 대댓글을 통해 CommentInfoDto 생성
         */

        commentInfoDtoList = commentListMap.keySet().stream() // (4)

                .map(comment -> new BoastCommentInfoDto(comment, commentListMap.get(comment)))
                .collect(Collectors.toList());  // (5)


    }
}