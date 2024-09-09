package org.ninhngoctuan.backend.service;

import org.ninhngoctuan.backend.dto.EmailActiveDTO;
import org.ninhngoctuan.backend.dto.UserDTO;

public interface UserService {
    UserDTO register(UserDTO userDTO);
    boolean sendEmail(UserDTO userDTO);
    boolean activeEmail(EmailActiveDTO emailActiveDTO);
    UserDTO info(String username);
}
