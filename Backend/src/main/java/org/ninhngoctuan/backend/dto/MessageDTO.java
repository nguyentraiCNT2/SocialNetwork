package org.ninhngoctuan.backend.dto;

import java.util.Date;

public class MessageDTO {
    private Long messageId;

    private UserDTO sender;

    private UserDTO receiver;

    private String content;

    private Date createdAt;

    // Getters and setters
}
