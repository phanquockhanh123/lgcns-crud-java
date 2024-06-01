package org.example.socialmediaspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.dto.*;
import org.example.socialmediaspring.entity.UserEntity;
import org.example.socialmediaspring.service.UserService;
import org.example.socialmediaspring.service.impl.UserServiceImpl;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody CreateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<UserResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> updateUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .result("User has been deleted")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<UserResponse> getUser(@PathVariable Long id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUser(id))
                .build();
    }
}
