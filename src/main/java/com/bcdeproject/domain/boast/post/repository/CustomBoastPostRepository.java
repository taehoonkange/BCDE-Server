package com.bcdeproject.domain.boast.post.repository;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import com.bcdeproject.global.condition.BoastPostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomBoastPostRepository {

    Page<BoastHashTag> search(BoastPostSearchCondition postSearchCondition, Pageable pageable);

    Page<BoastPost> getMyBoastPost(Member member, Pageable pageable);
}
