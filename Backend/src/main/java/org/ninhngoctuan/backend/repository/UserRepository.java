package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
    UserEntity findByPhone(String phone);
    boolean existsByEmail(String email);
    Optional<UserEntity> findByUserId(Long userid);
}
