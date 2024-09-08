package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "PostTags")
public class PostTagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postTagId;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private TagEntity tag;

    // Getters and setters
}
