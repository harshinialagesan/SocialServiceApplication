package com.ssa.repository;

import com.ssa.model.Post;
import com.ssa.model.Share;
import com.ssa.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShareRepository extends JpaRepository<Share,Long> {
    Long countSharesByPostId_Id(Long id);

    Optional<Share> findByPostIdAndUserId(Post post, User user);
}
