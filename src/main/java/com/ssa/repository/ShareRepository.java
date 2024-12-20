package com.ssa.repository;

import com.ssa.model.Likes;
import com.ssa.model.Post;
import com.ssa.model.Share;
import com.ssa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share,Long> {
    Long countSharesByPostId_Id(Long id);

    Optional<Share> findByPostIdAndUserId(Post post, User user);

    Page<Share> findAllByUserId_Id(Long userId, Pageable pageable);

    Page<Share> findByPostId(Post post, Pageable pageable);
}
