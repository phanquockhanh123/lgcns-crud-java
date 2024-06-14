package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUserNameOrEmail(String userName, String email);

    @Query(value = "SELECT * FROM users where id = :id ", nativeQuery = true)
    User findUserById(Long id);

    boolean existsById(Long id);

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query(value = "SELECT new org.example.socialmediaspring.dto.user.UserResponse(u.id, u.firstName, u.lastName, u.userName, u.address, u.email,u.phone, u.role) FROM User u ")
    Page<UserResponse> findUsersByConds(Pageable pageable);

    @Query(value = "SELECT * FROM users where user_name = :username ", nativeQuery = true)
    User findUsersByUsername(String username);

}
