package com.bcdeproject.domain.boast.like.repository;

import com.bcdeproject.domain.boast.like.BoastLike;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoastLikeRepository extends JpaRepository<BoastLike, Long> {

    Optional<BoastLike> findByMemberAndPost(Member member, BoastPost boastPost);
    int countByPost(BoastPost boastPost);
}
