package com.bcdeproject.domain.boast.imgurl.repository;

import com.bcdeproject.domain.boast.imgurl.BoastImgUrl;
import com.bcdeproject.domain.boast.post.BoastPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoastImgUrlRepository extends JpaRepository<BoastImgUrl, Long> {

    List<BoastImgUrl> findByPost(BoastPost post);
}
