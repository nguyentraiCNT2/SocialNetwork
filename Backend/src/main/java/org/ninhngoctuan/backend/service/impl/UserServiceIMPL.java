package org.ninhngoctuan.backend.service.impl;

import org.modelmapper.ModelMapper;
import org.ninhngoctuan.backend.config.PasswordValidator;
import org.ninhngoctuan.backend.config.RandomId;
import org.ninhngoctuan.backend.config.SendCodeByEmail;
import org.ninhngoctuan.backend.dto.EmailActiveDTO;
import org.ninhngoctuan.backend.dto.UserDTO;
import org.ninhngoctuan.backend.entity.EmailActiveEntity;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.EmailActiveRepository;
import org.ninhngoctuan.backend.repository.RoleRepository;
import org.ninhngoctuan.backend.repository.UserRepository;
import org.ninhngoctuan.backend.repository.UserSettingRepository;
import org.ninhngoctuan.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;

@Service
public class UserServiceIMPL implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserSettingRepository userSettingRepository;
    @Autowired
    private EmailActiveRepository emailActiveRepository;

    @Override
    public UserDTO register(UserDTO userDTO) {
        try {
            if (userDTO.getEmail() == null || userDTO.getEmail().equals(""))
                throw new RuntimeException("Email không thể để trống");
            if (userDTO.getPassword() == null || userDTO.getPassword().equals(""))
                throw new RuntimeException("Mật khẩu không thể để trống");
            if (userDTO.getPassword().length()< 6)
                throw new RuntimeException("Mật khẩu có tối thiểu 6 ký tự");
            if (userDTO.getPassword().length()> 255)
                throw new RuntimeException("Mật khẩu có tối đa 255 ký tự");
            if (!PasswordValidator.isValidPassword(userDTO.getPassword()))
                throw  new RuntimeException("Mật khẩu phải chứa chữ hoa, chữ thường và số");
            if (userDTO.getFullName() == null || userDTO.getFullName().equals(""))
                throw new RuntimeException("Họ tên không thể để trống");
            boolean checkEmail = userRepository.existsByEmail(userDTO.getEmail());
            if (checkEmail)
                throw new RuntimeException("Email này đã được sử dụng");
            RoleEntity role = roleRepository.findById(2L).orElseThrow(() -> new RuntimeException("Không có quyền hạn nào có id là: "+2));
            Date currentDate = new Date(System.currentTimeMillis());

            UserEntity user = modelMapper.map(userDTO, UserEntity.class);
            String hashPassword =  BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
            user.setCreatedAt(currentDate);
            user.setPassword(hashPassword);
            user.setActive(true);
            user.setEmailActive(false);
            user.setUpdatedAt(currentDate);
            user.setRoleId(role);
            UserEntity save_entity = userRepository.save(user);
            UserDTO dto = modelMapper.map(save_entity,UserDTO.class);
            sendEmail(dto);
            return  dto;

        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean sendEmail(UserDTO userDTO) {
        try {
            String code  = RandomId.generateMKC2(6);
            String to = userDTO.getEmail();
            String subject = "Xác thực email đăng ký ";
            String email = "Tài khoản của bạn có email là: " + userDTO.getEmail();
            String core = "Đây là mã xác thực của bạn: " + code;
            String bottom = "Xin cảm ơn!";
            String body = email + ". \n" + core + ". \n" + bottom;
            Boolean test = SendCodeByEmail.sendEmail(to, subject, body);
            if (test ==true){
                UserEntity userEntity  = modelMapper.map(userDTO, UserEntity.class);
                EmailActiveEntity entity = new EmailActiveEntity();
                entity.setEmail(userDTO.getEmail());
                entity.setOtp(code);
                entity.setUserId(userEntity);
                emailActiveRepository.save(entity);
                return true;
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException("Có lỗi sảy ra: "+e.getMessage());
        }
    }

    @Override
    public boolean activeEmail(EmailActiveDTO emailActiveDTO) {
        List<EmailActiveEntity> emailActiveEntities  = emailActiveRepository.findByEmail(emailActiveDTO.getEmail());
        if (emailActiveEntities.size() == 0)
            throw new RuntimeException("Không có tài khoản nào có email này");
        for (EmailActiveEntity active: emailActiveEntities){
            if (active.getOtp().equals(emailActiveDTO.getOtp())){
                UserEntity userEntity = userRepository.findByEmail(emailActiveDTO.getEmail());
                if (userEntity == null)
                    throw new RuntimeException("Không có tài khoản nào có email là: "+emailActiveDTO.getEmail());
                userEntity.setEmailActive(true);
                userRepository.save(userEntity);
                return true;
            }
        }
        return false;
    }

    @Override
    public UserDTO info(String username) {

        UserEntity userEntity = userRepository.findByEmail(username);
     UserDTO userDTO =  modelMapper.map(userEntity, UserDTO.class);
        userDTO.setPassword(null);
        return userDTO;
    }
}
