package org.ninhngoctuan.backend.entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "EventAttendees")
public class EventAttendeeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventAttendeeId;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    private EventEntity event;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "RSVP_status", nullable = false)
    private String rsvpStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Date createdAt;

    public enum RSVPStatus {
        ACCEPTED, DECLINED, TENTATIVE
    }

    // Getters and setters
}
