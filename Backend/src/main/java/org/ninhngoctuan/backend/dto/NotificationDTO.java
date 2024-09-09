package org.ninhngoctuan.backend.dto;

import java.util.Date;

public class NotificationDTO {
    private Long notificationId;

    private UserDTO user;

    private String type;

    private String content;

    private Boolean readStatus;

    private Date createdAt;

    // Getters and setters
}
