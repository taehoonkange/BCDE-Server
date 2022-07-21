package com.bcdeproject.domain.boast.heart;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_HEART")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BoastHeart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "heart_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private BoastPost post;

    // 연관 관계 편의 메소드
    public void confirmMember(Member member) {
        this.member = member;
        member.addBoastHeart(this);
    }

    public void confirmPost(BoastPost post) {
        this.post = post;
        post.addBoastHeart(this);
    }


}
