package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Boolean existsByUserNameOrEmail(String userName, String email);

    Optional<UserEntity> findById(Long id);

    boolean existsById(Long id);

    Optional<UserEntity> findByEmail(String email);

    Boolean existsByEmail(String email);
}
