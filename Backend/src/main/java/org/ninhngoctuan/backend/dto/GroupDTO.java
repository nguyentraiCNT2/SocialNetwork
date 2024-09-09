package org.ninhngoctuan.backend.dto;

import jakarta.persistence.*;

import java.util.Date;
import java.util.Set;

public class GroupDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "group")
    private Set<GroupMemberDTO> groupMembers;

    // Getters and setters
}
