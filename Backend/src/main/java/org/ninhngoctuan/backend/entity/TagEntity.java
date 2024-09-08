package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "Tags")
public class TagEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    // Getters and setters
}
