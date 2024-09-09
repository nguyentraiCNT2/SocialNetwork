package org.ninhngoctuan.backend.dto;

import java.util.Date;

public class FriendDTO {
    private Long friendshipId;

    private UserDTO user;

    private UserDTO friend;

    private String status;

    private Date createdAt;

    public enum FriendshipStatus {
        PENDING, ACCEPTED, REJECTED
    }

    // Getters and setters
}
