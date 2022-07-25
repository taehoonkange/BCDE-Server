package com.bcdeproject.domain.boast.comment.service;

import com.bcdeproject.domain.boast.comment.BoastComment;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentSaveDto;
import com.bcdeproject.domain.boast.comment.dto.BoastCommentUpdateDto;
import com.bcdeproject.domain.boast.comment.exception.BoastCommentException;
import com.bcdeproject.domain.boast.comment.exception.BoastCommentExceptionType;
import com.bcdeproject.domain.boast.comment.repository.BoastCommentRepository;
import com.bcdeproject.domain.boast.post.exception.BoastPostException;
import com.bcdeproject.domain.boast.post.exception.BoastPostExceptionType;
import com.bcdeproject.domain.boast.post.repository.BoastPostRepository;
import com.bcdeproject.domain.member.exception.MemberException;
import com.bcdeproject.domain.member.exception.MemberExceptionType;
import com.bcdeproject.domain.member.repository.MemberRepository;
import com.bcdeproject.global.util.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BoastCommentServiceImpl implements BoastCommentService{

    private final BoastCommentRepository boastCommentRepository;
    private final MemberRepository memberRepository;
    private final BoastPostRepository boastPostRepository;

    /**
     * 댓글 저장 로직
     * 컨트롤러에서 postId, BoastCommentSaveDto를 받고,
     * BoastCommentSaveDto에서 content를 꺼내서 빌더로 BoastComment 객체 생성
     * 생성한 객체에 writer, post 추가한 후 DB에 저장
     */
    @Override
    public void save(Long postId, BoastCommentSaveDto boastCommentSaveDto) {
        BoastComment comment = BoastComment.builder().content(boastCommentSaveDto.getContent()).build();

        comment.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmPost(boastPostRepository.findById(postId).orElseThrow(() -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND)));

        boastCommentRepository.save(comment);
    }

    /**
     * 대댓글 저장 로직
     * 컨트롤러에서 postId, parentId(부모 댓글 id), BoastCommentSaveDto를 받고,
     * BoastCommentSaveDto에서 content를 꺼내서 빌더로 BoastComment 객체 생성
     * 생성한 객체에 writer, post, parent(부모 댓글) 추가한 후 DB에 저장
     */
    @Override
    public void saveReComment(Long postId, Long parentId, BoastCommentSaveDto boastCommentSaveDto) {
        BoastComment comment = BoastComment.builder().content(boastCommentSaveDto.getContent()).build();

        comment.confirmWriter(memberRepository.findByUsername(SecurityUtil.getLoginUsername()).orElseThrow(() -> new MemberException(MemberExceptionType.NOT_FOUND_MEMBER)));

        comment.confirmPost(boastPostRepository.findById(postId).orElseThrow(() -> new BoastPostException(BoastPostExceptionType.POST_NOT_FOUND)));

        comment.confirmParent(boastCommentRepository.findById(parentId).orElseThrow(() -> new BoastCommentException(BoastCommentExceptionType.NOT_FOUND_COMMENT)));

        boastCommentRepository.save(comment);
    }


    /**
     * 댓글 업데이트 로직
     * 컨트롤러에서 Id(댓글 id), BoastCommentUpdateDto를 받고,
     * id에 해당하는 댓글을 DB에서 찾은 후, 내용 수정
     */
    @Override
    public void update(Long id, BoastCommentUpdateDto boastCommentUpdateDto) {

        BoastComment comment = boastCommentRepository.findById(id).orElseThrow(() -> new BoastCommentException(BoastCommentExceptionType.NOT_FOUND_COMMENT));
        if(!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUsername())){
            throw new BoastCommentException(BoastCommentExceptionType.NOT_AUTHORITY_UPDATE_COMMENT);
        }

        boastCommentUpdateDto.getContent().ifPresent(comment::updateContent);
    }


    /**
     * 댓글 삭제 로직
     * 컨트롤러에서 Id(댓글 id)를 받고
     * id에 해당하는 댓글을 찾아서 삭제
     */
    @Override
    public void remove(Long id) throws BoastCommentException {
        BoastComment comment = boastCommentRepository.findById(id).orElseThrow(() -> new BoastCommentException(BoastCommentExceptionType.NOT_FOUND_COMMENT));

        if(!comment.getWriter().getUsername().equals(SecurityUtil.getLoginUsername())){
            throw new BoastCommentException(BoastCommentExceptionType.NOT_AUTHORITY_DELETE_COMMENT);
        }

        comment.remove();
        List<BoastComment> removableCommentList = comment.findRemovableList();
        boastCommentRepository.deleteAll(removableCommentList);
    }
}
