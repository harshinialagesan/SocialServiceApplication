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

    Page<Post> findByTags_IdInAndIsActive(List<Long> tagIds, Integer isActive,Pageable pageable);

    Page<Post> findAll(Specification<Post> combinedSpecification, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndTags_IdInAndIsActive(String title, List<Long> tagId,Integer isActive, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCaseAndIsActive(String title,Integer isActive, Pageable pageable);

    Page<Post> findAllByIsActive(Integer isActive,Pageable pageable);
}
