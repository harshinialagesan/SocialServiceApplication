package com.ssa.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tags")
@NoArgsConstructor
@AllArgsConstructor
public class Tag extends BaseModel{

    @Column(name = "name", unique = true, length = 300)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User userid;
}
