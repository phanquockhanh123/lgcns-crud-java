package org.example.socialmediaspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.common.ApiResponse;
import org.example.socialmediaspring.dto.user.CreateUserRequest;
import org.example.socialmediaspring.dto.user.UpdateUserRequest;
import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ResponseFactory responseFactory;

    @PostMapping
    public ResponseEntity createUser(@RequestBody CreateUserRequest request) {

        return responseFactory.success(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long id) {
        return responseFactory.success(userService.updateUser(request, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return responseFactory.success(null);

    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        return responseFactory.success(userService.getUser(id));
    }
}
