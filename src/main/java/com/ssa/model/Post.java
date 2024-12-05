package com.ssa.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Entity
@Table(name = "post")
@NoArgsConstructor
@AllArgsConstructor
public class Post extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "description")
    private String description;
    @ManyToMany
    @JoinTable(name = "post_image", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "images_id"))
    private List<Images> imageUrl;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;
    @ManyToMany
    @JoinTable(name = "post_tags", joinColumns = @JoinColumn(name = "post_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
    @Transient
    private Long likeCount;


}
