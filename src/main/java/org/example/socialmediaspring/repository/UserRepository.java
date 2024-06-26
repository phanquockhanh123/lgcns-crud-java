package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.user.BestCustomerRes;
import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.Role;
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

    @Query(value = "SELECT new org.example.socialmediaspring.dto.user.UserResponse(u.id, u.firstName, u.lastName, u.userName, u.address, u.email,u.phone, u.role)  FROM User u " +
            " WHERE (:role is null OR u.role = :role) " +
            " AND (:email is null OR u.email LIKE CONCAT('%', :email, '%'))")
    Page<UserResponse> findUsersByConds(Pageable pageable, Role role, String email);

    @Query(value = "SELECT * FROM users where user_name = :username ", nativeQuery = true)
    User findUsersByUsername(String username);


    @Query(value = "SELECT new org.example.socialmediaspring.dto.user.BestCustomerRes(u.id, u.email, u.phone, u.address, SUM(bt.quantity) as total_buy, SUM(bt.amount + bt.bonus) AS total_money) " +
            " FROM BookTransaction bt " +
            " JOIN User u " +
            " ON bt.userId = u.id " +
            " WHERE bt.status = 1 " +
            " GROUP BY u.id " +
            " ORDER BY total_money DESC " +
            " LIMIT 10 ")
    List<BestCustomerRes> getBestCustomer();


}
