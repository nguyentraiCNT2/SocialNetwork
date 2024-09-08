package org.ninhngoctuan.backend.config;

import jakarta.annotation.PostConstruct;
import org.ninhngoctuan.backend.entity.RoleEntity;
import org.ninhngoctuan.backend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
    @Autowired
    private RoleRepository roleRepository;
    @PostConstruct
    public void init() {
        if (roleRepository.count() == 0) {
            RoleEntity adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);

            RoleEntity userRole = new RoleEntity();
            userRole.setName("USER");
            roleRepository.save(userRole);
        }

    }
}
