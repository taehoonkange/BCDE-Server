package com.bcdeproject.domain.boast.comment;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_COMMENT")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoastComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private BoastPost post;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private BoastComment parent;

    @Lob
    @Column(nullable = false)
    private String content;

    private boolean isRemoved = false;

    // 부모 댓글을 삭제해도 자식 댓글은 남아 있어야하므로
    // cascade, orphanRemoval 설정 X
    @OneToMany(mappedBy = "parent")
    private List<BoastComment> childBoastCommentList = new ArrayList<>();

    // 연관 관계 편의 메소드
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoastComment(this);
    }

    public void confirmPost(BoastPost post) {
        this.post = post;
        post.addBoastComment(this);
    }

    public void confirmParent(BoastComment parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addChild(BoastComment child) {
        childBoastCommentList.add(child);
    }

    // 수정
    public void updateContent(String content) {
        this.content = content;
    }

    // 삭제
    public void remove() {
        this.isRemoved = true;
    }

    @Builder
    public BoastComment(Member writer, BoastPost post, BoastComment parent, String content) {
        this.writer = writer;
        this.post = post;
        this.parent = parent;
        this.content = content;
        this.isRemoved = false;
    }

    // 비즈니스 로직

    // 모든 자식 댓글(대댓글)이 삭제되었는지 판단
    private boolean isAllChildRemoved() {
        Boolean isAllChildRemoved = getChildBoastCommentList().stream()
                .map(BoastComment::isRemoved) // = () -> RecommendComment.isRemoved()의 결과로 리스트의 Value를 바꾼다. (지워졌는지 여부)
                .filter(isRemove -> !isRemove) // isRemoved()의 결과 중에서 필터로 false인 결과만 가져온다.
                .findAny() // false인 결과가 하나라도 있다면(지워지지 않은 댓글이 하나라도 있으면) false 반환
                .orElse(true);// 결과가 모두 true라면(모두 다 지워진 댓글이라면) true 반환

        return isAllChildRemoved;
    }

    public List<BoastComment> findRemovableList() {
        List<BoastComment> result = new ArrayList<>();

        Optional.ofNullable(this.parent).ifPresentOrElse(
                parentBoastComment -> {
                    // 대댓글인 경우 (부모가 존재하는(Present) 경우)
                    if(parentBoastComment.isRemoved() && parentBoastComment.isAllChildRemoved()) { // 댓글이 지워졌고, 모든 대댓글도 지워졌다면
                        // 지울 댓글들 리스트에 모두 저장
                        result.addAll(parentBoastComment.getChildBoastCommentList());
                        result.add(parentBoastComment);
                    }
                },

                // 댓글인 경우 (부모가 존재하지 않는(Else)의 경우)
                () -> {
                    if(isAllChildRemoved()) {
                        // 지울 댓글들 리스트에 모두 저장
                        result.add(this);
                        result.addAll(this.getChildBoastCommentList());
                    }
                }
        );
        // 지울 댓글들 리스트 반환
        return result;
    }
}
