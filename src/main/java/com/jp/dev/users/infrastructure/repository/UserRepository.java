package com.jp.dev.users.infrastructure.repository;

import com.jp.dev.users.infrastructure.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

  Optional<UserEntity> findByUsername(String username);

  List<UserEntity> findAllByApp(String app);
}
