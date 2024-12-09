package com.ssa.repository;

import com.ssa.model.Post;
import com.ssa.model.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    boolean existsByTagsContains(Tag tagToRemove);

    Page<Post> findByTags_IdIn(List<Long> tagIds, Pageable pageable);

    Page<Post> findAll(Specification<Post> combinedSpecification, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndTags_IdIn(String title, List<Long> tagId, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
