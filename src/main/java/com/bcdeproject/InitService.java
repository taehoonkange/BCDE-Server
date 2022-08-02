//
//package com.bcdeproject;
//
//import com.bcdeproject.domain.boast.comment.BoastComment;
//import com.bcdeproject.domain.boast.comment.repository.BoastCommentRepository;
//import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
//import com.bcdeproject.domain.boast.post.BoastPost;
//import com.bcdeproject.domain.boast.post.repository.BoastPostRepository;
//import com.bcdeproject.domain.member.Member;
//import com.bcdeproject.domain.member.Role;
//import com.bcdeproject.domain.member.repository.MemberRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//
//import java.util.List;
//
//import static java.lang.Long.parseLong;
//import static java.lang.String.format;
//import static java.lang.String.valueOf;
//
//
////더미 데이터 생성 클래스
//@RequiredArgsConstructor
//@Component
//public class InitService{
//
//    private final Init init;
//
//    @PostConstruct
//    public void init() {
//
//        init.save();
//    }
//
//    @RequiredArgsConstructor
//    @Component
//    @Slf4j
//    private static class Init{
//        private final MemberRepository memberRepository;
//
//        private final BoastPostRepository boastPostRepository;
//        private final BoastCommentRepository boastCommentRepository;
//
//            @Transactional
//            public void save() {
//                PasswordEncoder delegatingPasswordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//
//                //== 멤버 저장 ==//
//                memberRepository.save(Member.builder().username("username1").password(delegatingPasswordEncoder.encode("password123#")).nickName("KSH1").role(Role.USER).build());
//                memberRepository.save(Member.builder().username("username2").password(delegatingPasswordEncoder.encode("password123#")).nickName("KSH2").role(Role.USER).build());
//                memberRepository.save(Member.builder().username("username3").password(delegatingPasswordEncoder.encode("password123#")).nickName("KSH3").role(Role.USER).build());
//
//                Member member = memberRepository.findById(1L).orElse(null);
//                log.info("멤버 저장 : {}", member);
//
//
//                for(int i = 0; i<=50; i++ ){
//
//                    BoastPost post = BoastPost.builder().title(format("게시글 %s", i)).content(format("내용 %s", i)).build();
//                    BoastHashTag hashTag = BoastHashTag.builder().name(format("해시태그 %s", i)).build();
//                    hashTag.confirmPost(post, hashTag);
//                    post.confirmWriter(memberRepository.findById((long) (i % 3 + 1)).orElse(null));
//                    boastPostRepository.save(post);
//                }
//
//                for(int i = 1; i<=50; i++ ){
//                    BoastComment comment = BoastComment.builder().content("댓글" + i).build();
//                    comment.confirmWriter(memberRepository.findById((long) (i % 3 + 1)).orElse(null));
//
//                    comment.confirmPost(boastPostRepository.findById(parseLong(valueOf(i%50 + 1))).orElse(null));
//                    boastCommentRepository.save(comment);
//                }
//
//
//                boastCommentRepository.findAll().stream().forEach(comment -> {
//
//                    for(int i = 1; i<=50; i++ ){
//                        BoastComment reComment = BoastComment.builder().content("대댓글" + i).build();
//                        reComment.confirmWriter(memberRepository.findById((long) (i % 3 + 1)).orElse(null));
//
//                        reComment.confirmPost(comment.getPost());
//                        reComment.confirmParent(comment);
//                        boastCommentRepository.save(reComment);
//                    }
//
//            });
//        }
//    }
//}
//
//
