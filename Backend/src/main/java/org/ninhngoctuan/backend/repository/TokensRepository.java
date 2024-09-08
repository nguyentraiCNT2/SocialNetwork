package org.ninhngoctuan.backend.repository;

import org.ninhngoctuan.backend.entity.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokensRepository extends JpaRepository<TokenEntity, Long> {
}
