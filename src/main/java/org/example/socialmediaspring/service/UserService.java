package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.ApiResponse;
import org.example.socialmediaspring.dto.CreateUserRequest;
import org.example.socialmediaspring.dto.UpdateUserRequest;
import org.example.socialmediaspring.dto.UserResponse;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);

    UserResponse updateUser(UpdateUserRequest request, Long userId);

    void deleteUser(Long userId);

    UserResponse getUser(Long userId);
}
