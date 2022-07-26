package com.bcdeproject.domain.boast.post.repository;

import com.bcdeproject.domain.boast.post.BoastPost;
import com.bcdeproject.global.condition.BoastPostSearchCondition;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;

import java.util.List;

import static com.bcdeproject.domain.boast.post.QBoastPost.boastPost;
import static com.bcdeproject.domain.member.QMember.member;

public class CustomBoastPostRepositoryImpl implements CustomBoastPostRepository{

    private final JPAQueryFactory query;
    public CustomBoastPostRepositoryImpl(EntityManager em) {
        query = new JPAQueryFactory(em);
    }


    @Override
    public Page<BoastPost> search(BoastPostSearchCondition postSearchCondition, Pageable pageable) {



        List<BoastPost> content = query.selectFrom(boastPost)

                .where(
                        // null 이면 조건 무시
                        contentHasStr(postSearchCondition.getContent()), // boastPost.content.contains(content)
                        titleHasStr(postSearchCondition.getTitle()) // boastPost.title.contains(title)
//                        hashTagHasStr(postSearchCondition.getHashTag()) // boastPost.content.contains(hashTag)
                )
                .leftJoin(boastPost.writer, member)

                .fetchJoin()
                .orderBy(boastPost.createdDate.desc())//최신 날짜부터
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch(); //Count 쿼리 발생 X




        JPAQuery<BoastPost> countQuery = query.selectFrom(boastPost)
                .where(
                        contentHasStr(postSearchCondition.getContent()),
                        titleHasStr(postSearchCondition.getTitle())
                );


        return PageableExecutionUtils.getPage(content, pageable, () -> countQuery.fetch().size());
    }

    /**
     * content, title, hashTag가 문자인지 검증
     * 문자라면, boastPost.content.contains(content) QueryDSL문 리턴
     * 문자가 아니라면, null 리턴
     */
    private BooleanExpression contentHasStr(String content) {
        return StringUtils.hasLength(content) ? boastPost.content.contains(content) : null;
    }


    private BooleanExpression titleHasStr(String title) {
        return StringUtils.hasLength(title) ? boastPost.title.contains(title) : null;
    }
//
//    private BooleanExpression hashTagHasStr(String hashTag) {
//        return StringUtils.hasLength(hashTag) ? boastPost.boastHashTagList : null;
//    }

}
