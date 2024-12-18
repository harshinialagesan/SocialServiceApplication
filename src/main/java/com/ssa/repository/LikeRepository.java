package com.ssa.repository;

import com.ssa.model.Likes;
import com.ssa.model.Post;
import com.ssa.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByPostIdAndUserId(Post post, User user);
    Page<Likes> findByUserId_Id(Long userId, Pageable pageable);

    long countByUserId_Id(Long userId);

    Page<Likes> findByPostId(Post post, Pageable pageable);
}
