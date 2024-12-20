package com.ssa.repository;


import com.ssa.model.Images;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Images, Long> {
}
