package org.example.socialmediaspring.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.user.UserRequest;
import org.example.socialmediaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ResponseFactory responseFactory;

    @PostMapping
    public ResponseEntity createUser(@RequestBody UserRequest request) {

        return responseFactory.success(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateUser(@RequestBody UserRequest request, @PathVariable Long id) {
        return responseFactory.success(userService.updateUser(request, id));
    }

    @DeleteMapping
    public ResponseEntity deleteUsersByIds(@RequestBody LongIdsRequest ids) {

        return responseFactory.success( userService.deleteUsersByIds(ids));

    }

    @GetMapping("/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        return responseFactory.success(userService.getUser(id));
    }

    @GetMapping("/search")
    public ResponseEntity findUsersByConds(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size
    ) {
        return responseFactory.success(userService.findUsers(page, size));
    }

}
