package com.ssa.repository;

import com.ssa.model.Comment;
import com.ssa.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostId(Post post);
}
