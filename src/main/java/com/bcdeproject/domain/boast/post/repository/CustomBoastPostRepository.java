package com.bcdeproject.domain.boast.post.repository;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import com.bcdeproject.global.condition.BoastPostSearchCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomBoastPostRepository {

    List<BoastPost> searchByHashTag(BoastPostSearchCondition postSearchCondition);

    List<BoastPost> getRecentBoastPost(Member member);

}
