package com.bcdeproject.domain.boast.imgurl;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_imgUrl")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class BoastImgUrl extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgUrl_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private BoastPost post;

    private String imgUrl;

    // 연관 관계 편의 메소드
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoastImgUrl(this);
    }

    public void confirmPost(BoastPost post) {
        this.post = post;
        post.addBoastImgUrl(this);
    }

    // 수정
    public void updateImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
