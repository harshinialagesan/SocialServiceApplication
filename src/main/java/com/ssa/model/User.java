package com.ssa.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssa.constant.Constants;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@Entity
@Table(name = "user_details")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "user_email")
    public String userEmail;

    @Column(name = "user_password")
    public String userPassword;

    @Column(name = "user_name")
    public String userName;

    @Column(name = "user_number")
    public String userNumber;

    @Column(name = "user_dob")
    public String userDob;

    @Column(name = "user_age")
    public String userAge;

    @Column(name = "is_active")
    public Integer isActive = Constants.IS_ACTIVE;

}
