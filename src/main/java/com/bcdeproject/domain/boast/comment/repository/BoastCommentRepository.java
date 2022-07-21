package com.bcdeproject.domain.boast.comment.repository;

import com.bcdeproject.domain.boast.comment.BoastComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoastCommentRepository extends JpaRepository<BoastComment, Long> {
}
