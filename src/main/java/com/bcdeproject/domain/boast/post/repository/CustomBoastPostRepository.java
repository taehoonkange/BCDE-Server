package com.bcdeproject.domain.boast.post.repository;

import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.boast.post.condition.BoastPostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBoastPostRepository {

    Page<BoastPost> search(BoastPostSearchCondition postSearchCondition, Pageable pageable);
}
