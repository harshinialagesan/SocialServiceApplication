package com.ssa.repository;

import com.ssa.model.Share;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShareRepository extends JpaRepository<Share,Long> {
    Long countSharesByPostId_Id(Long id);
}
