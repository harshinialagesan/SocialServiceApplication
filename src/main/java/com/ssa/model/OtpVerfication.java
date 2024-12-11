package com.ssa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Entity
@Getter
@Setter
@Table(name = "otp_verifications")
@NoArgsConstructor
@AllArgsConstructor
public class OtpVerfication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "otp", nullable = false)
    private String otp;

    @Column(name = "expirationTime", nullable = false)
    private LocalDateTime expirationTime;

    @Column(name = "verified")
    private Integer verified;
}
