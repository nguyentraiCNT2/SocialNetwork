// CustomUserDetailsService.java
package org.ninhngoctuan.backend.securityConfig;

import org.ninhngoctuan.backend.context.RequestContext;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.entity.UserEntity;
import org.ninhngoctuan.backend.repository.RoleRepository;
import org.ninhngoctuan.backend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    @Lazy
    private UserRepository userRepository;

    @Autowired
    @Lazy
    private RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity;
        String emailorPhone = "";
        if (username.contains("@")) {
            userEntity = userRepository.findByEmail(username);
            emailorPhone = userEntity.getEmail();
        } else {
            userEntity = userRepository.findByPhone(username);
            emailorPhone = userEntity.getPhone();
        }

        if (userEntity == null) {
            throw new UsernameNotFoundException("Tài khoản không tồn tại");
        }

        RequestContext context = RequestContext.get();
        context.setUserId(userEntity.getUserId());

        RoleEntity roleEntity = roleRepository.findById(userEntity.getRoleId().getId())
                .orElseThrow(() -> new UsernameNotFoundException("Không có quyền hạn này"));

        return User.withUsername(emailorPhone)
                .password(userEntity.getPassword())
                .roles(roleEntity.getName())
                .build();
    }
}