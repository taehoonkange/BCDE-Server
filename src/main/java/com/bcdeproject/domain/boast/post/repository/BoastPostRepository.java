package com.bcdeproject.domain.boast.post.repository;

import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoastPostRepository extends JpaRepository<BoastPost, Long>, CustomBoastPostRepository {

    Optional<BoastPost> findById(Long id);

    Optional<BoastPost> findByWriterId(Long id);

    @EntityGraph(attributePaths = {"writer"})
    Optional<BoastPost> findWithWriterById(Long id);
}
