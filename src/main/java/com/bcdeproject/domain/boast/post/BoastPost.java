package com.bcdeproject.domain.boast.post;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.like.BoastLike;
import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import com.bcdeproject.domain.member.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_POST")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class BoastPost extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(length = 40, nullable = false)
    private String title;

    @Lob
    @Column(length = 500, nullable = false)
    private String content;

    @ColumnDefault("0")
    private int likeCount;

    // 게시글 삭제 시 달려있는 댓글, 해시태그, 좋아요, 이미지 모두 삭제
    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastComment> boastCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastHashTag> boastHashTagList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastLike> boastLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastImgUrl> boastImgUrlList = new ArrayList<>();


    // 연관 관계 편의 메소드
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoastPost(this);
    }

    public void addBoastComment(BoastComment boastComment) {
        boastCommentList.add(boastComment);
    }

    public void addBoastHashTag(BoastHashTag boastHashTag) {
        boastHashTagList.add(boastHashTag);
    }

    public void addBoastLike(BoastLike boastLike) {
        boastLikeList.add(boastLike);
    }

    public void addBoastImgUrl(BoastImgUrl boastImgUrl) {
        boastImgUrlList.add(boastImgUrl);
    }

    public void removeBoastImgUrl(BoastImgUrl boastImgUrl) {
        boastImgUrlList.remove(boastImgUrl);
    }

    public void addLike() { this.likeCount += 1;}

    public void deleteLike() { this.likeCount -= 1; }

    // 내용 수정
    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

}
