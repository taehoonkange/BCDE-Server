package com.bcdeproject.domain.boast.post.repository;

import com.bcdeproject.domain.boast.hashtag.BoastHashTag;
import com.bcdeproject.domain.boast.like.QBoastLike;
import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.domain.member.Member;
import com.bcdeproject.domain.member.QMember;
import com.bcdeproject.global.condition.BoastPostSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.bcdeproject.domain.boast.hashtag.QBoastHashTag.boastHashTag;
import static com.bcdeproject.domain.boast.like.QBoastLike.boastLike;
import static com.bcdeproject.domain.boast.post.QBoastPost.boastPost;
import static com.bcdeproject.domain.member.QMember.member;

@Repository
public class CustomBoastPostRepositoryImpl implements CustomBoastPostRepository{

    private final JPAQueryFactory query;
    public CustomBoastPostRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }

    /**
     * 해시태그 검색 로직
     * 검색 해시태그를 포함하는 HashTag 게시물을 찾아서 Post와 fetchjoin후 최신 날짜부터 정렬해서 Paging
     */
    @Override
    public List<BoastHashTag> searchByHashTag(BoastPostSearchCondition postSearchCondition) {

        List<BoastHashTag> hashTagList = query.selectFrom(boastHashTag)
                .where(
                        // null 이면 조건 무시
                        hashTagHasStr(postSearchCondition.getHashTag()) // boastHashTag.name.contains(hashTag)
                )
                .leftJoin(boastHashTag.post, boastPost)
                .fetchJoin()
                .orderBy(boastPost.createdDate.desc())//최신 날짜부터
                .fetch(); //Count 쿼리 발생 X


        return hashTagList;
    }

    /**
     * hashTag가 문자인지 검증
     * 문자라면, boastPost.content.contains(content) QueryDSL문 리턴
     * 문자가 아니라면, null 리턴
     */
    private BooleanExpression hashTagHasStr(String hashTag) {

        return StringUtils.hasLength(hashTag) ? boastHashTag.name.contains(hashTag) : null;
    }

    @Override
    public List<BoastPost> getRecentBoastPost(Member member) {

        List<BoastPost> recentBoastPostList = query.selectFrom(boastPost)
                .orderBy(boastPost.createdDate.desc())
                .fetch();

        return recentBoastPostList;
    }

}
