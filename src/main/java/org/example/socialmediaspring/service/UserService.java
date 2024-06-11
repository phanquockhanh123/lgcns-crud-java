package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.user.CreateUserRequest;
import org.example.socialmediaspring.dto.user.UpdateUserRequest;
import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.entity.User;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    User getUser(Long userId);

}
