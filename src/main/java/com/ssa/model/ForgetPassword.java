//package com.ssa.model;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.util.Date;
//
//@Getter
//@Setter
//@Entity
//@Table(name = "forget_password")
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class ForgetPassword {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//
//    @Column(name = "otp", nullable = false)
//    private Long otp;
//
//    @Column(name = "expiration_time", nullable = false)
//    private Date expirationTime;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//}
