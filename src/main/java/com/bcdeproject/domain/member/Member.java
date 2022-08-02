package com.bcdeproject.domain.member;

import com.bcdeproject.domain.BaseTimeEntity;
import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.boast.like.BoastLike;
import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import com.bcdeproject.domain.boast.post.BoastPost;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Table(name = "MEMBER")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@AllArgsConstructor
@Builder
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id; //primary Key

    @Column(nullable = false, length = 30, unique = true)
    private String username; // 아이디

    private String password;// 비밀번호

    @Column(nullable = false, length = 30)
    private String nickName;//별명

    @Column(length = 3000)
    private String profileImgUrl; // 프로필 사진

    @Column(length = 1000)
    private String refreshToken;//RefreshToken

    @Enumerated(EnumType.STRING)
    private Role role;//권한 -> USER, ADMIN

    // 회원 탈퇴 시 사용 -> 작성 게시물, 댓글, 해시태그, 이미지 / 누른 좋아요 모두 삭제
    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastPost> boastPostList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastComment> boastCommentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastLike> boastLikeList = new ArrayList<>();

    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
    @Builder.Default
    private List<BoastImgUrl> boastImgUrlList = new ArrayList<>();

//    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
//    private List<VotePost> votePostList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "writer", cascade = ALL, orphanRemoval = true)
//    private List<VoteComment> voteCommentList = new ArrayList<>();

    // 연관 관계 메소드
    public void addBoastPost(BoastPost boastPost){
        //post의 writer 설정은 post에서 함
        boastPostList.add(boastPost);
    }

    public void addBoastComment(BoastComment boastComment){
        //comment의 writer 설정은 comment에서 함
        boastCommentList.add(boastComment);
    }

    public void addBoastLike(BoastLike boastLike) {
        //like의 member 설정은 like에서 함
        boastLikeList.add(boastLike);
    }

    public void addBoastImgUrl(BoastImgUrl boastImgUrl) {
        //imgUrl의 writer 설정은 imgUrl에서 함
        boastImgUrlList.add(boastImgUrl);
    }



//    public void addVotePost(VotePost votePost){
//        //post의 writer 설정은 post에서 함
//        votePostList.add(votePost);
//    }
//
//    public void addVoteComment(VoteComment voteComment){
//        //comment의 writer 설정은 comment에서 함
//        voteCommentList.add(voteComment);
//    }





    //== 정보 수정 ==//
    public void updatePassword(PasswordEncoder passwordEncoder, String password){
        this.password = passwordEncoder.encode(password);
    }

    public void updateNickName(String nickName){
        this.nickName = nickName;
    }

    public void updateProfileImgUrl(String profileImgUrl){
        this.profileImgUrl = profileImgUrl;
    }

    public void updateRefreshToken(String refreshToken){
        this.refreshToken = refreshToken;
    }
    public void destroyRefreshToken(){
        this.refreshToken = null;
    }

    //== 패스워드 암호화 ==//
    public void encodePassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(password);
    }

    //비밀번호 변경, 회원 탈퇴 시, 비밀번호를 확인하며, 이때 비밀번호의 일치여부를 판단하는 메서드
    public boolean matchPassword(PasswordEncoder passwordEncoder, String checkPassword){
        return passwordEncoder.matches(checkPassword, getPassword());
    }


    //회원가입시, USER의 권한을 부여하는 메서드
    public void addUserAuthority() {
        this.role = Role.USER;
    }


}