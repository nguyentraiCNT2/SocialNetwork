package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "Friends")
public class FriendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long friendshipId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "friend_id", nullable = false)
    private UserEntity friend;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    public enum FriendshipStatus {
        PENDING, ACCEPTED, REJECTED
    }

    // Getters and setters
}
