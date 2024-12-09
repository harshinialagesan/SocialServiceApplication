package com.ssa.repository;


import com.ssa.model.OtpVerfication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<OtpVerfication,Long> {
    Optional<OtpVerfication> findByEmailAndOtp(String email, String otp);
}
