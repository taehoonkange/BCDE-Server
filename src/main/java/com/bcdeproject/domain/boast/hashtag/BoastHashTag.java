package com.bcdeproject.domain.boast.hashtag;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @JoinColumn(name = "post_id")
    @JsonBackReference
    private BoastPost post;

    @Column
    private String name;


    // 연관 관계 편의 메소드
    public void confirmPost(BoastPost post, BoastHashTag boastHashTag) {
        this.post = post;
        post.addBoastHashTag(boastHashTag);
    }

    // 수정
    public void updateName(String name) {
        this.name = name;
    }

}
