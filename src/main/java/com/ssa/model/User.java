package com.ssa.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssa.constant.Constants;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name = "user_email")
    public String userEmail;

    @Column(name = "user_password")
    public String userPassword;

    @Column(name = "user_name")
    public String userName;

    @Column(name = "name")
    public String name;

    @OneToMany(mappedBy = "user")
    private List<ForgetPassword> forgetPassword;


}
