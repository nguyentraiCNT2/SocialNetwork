package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "GroupMembers")
public class GroupMemberEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupMemberId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private GroupEntity group;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private Date joinedAt;

    // Getters and setters
}
