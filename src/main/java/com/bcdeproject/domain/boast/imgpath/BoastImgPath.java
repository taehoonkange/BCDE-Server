package com.bcdeproject.domain.boast.imgpath;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_imgPath")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class BoastImgPath extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imgPath_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private BoastPost post;

    private String imgPath;

    // 연관 관계 편의 메소드
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoastImgPath(this);
    }

    public void confirmPost(BoastPost post) {
        this.post = post;
        post.addBoastImgPath(this);
    }

    // 수정
    public void updateImgPath(String imgPath) {
        this.imgPath = imgPath;
    }
}
