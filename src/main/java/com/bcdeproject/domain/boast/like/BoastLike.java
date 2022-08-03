package com.bcdeproject.domain.boast.like;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_LIKE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Builder
public class BoastLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private BoastPost post;

    private boolean isLike;


    // 연관 관계 편의 메소드
    public void confirmMember(Member member) {
        this.member = member;
        member.addBoastLike(this);
    }

    public void confirmPost(BoastPost post) {
        this.post = post;
        post.addBoastLike(this);
    }

    @Builder
    public BoastLike(Member member, BoastPost boastPost) {
        this.member = member;
        this.post = boastPost;
    }

}
