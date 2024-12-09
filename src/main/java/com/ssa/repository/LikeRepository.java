package com.ssa.repository;

import com.ssa.model.Likes;
import com.ssa.model.Post;
import com.ssa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Likes,Long> {
    Optional<Likes> findByPostIdAndUserId(Post post, User user);
    List<Likes> findByUserId_Id(Long userId);
}
