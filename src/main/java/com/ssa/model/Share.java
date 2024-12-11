package com.ssa.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "share")
@NoArgsConstructor
@AllArgsConstructor
public class Share extends BaseModel{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id",nullable = false)
    @JsonBackReference
    private Post postId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    @JsonBackReference
    private User userId;
    @Column(name = "comment")
    private String commentOnShare;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "shared_at")
    @CreationTimestamp
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSSSSS", timezone = "UTC")
    private LocalDateTime sharedAt;

}
