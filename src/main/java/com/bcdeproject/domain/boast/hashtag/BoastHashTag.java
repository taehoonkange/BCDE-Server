package com.bcdeproject.domain.boast.hashtag;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Table(name = "BOAST_HASHTAG")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class BoastHashTag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashTag_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private BoastPost post;

    @Column
    private String name;


    // 연관 관계 편의 메소드
    public void confirmWriter(Member writer) {
        this.writer = writer;
        writer.addBoastHashTag(this);
    }

    public void confirmPost(BoastPost post) {
        this.post = post;
        post.addBoastHashTag(this);
    }

    // 수정
    public void updateName(String name) {
        this.name = name;
    }

}
