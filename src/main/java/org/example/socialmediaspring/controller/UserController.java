package org.example.socialmediaspring.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.ResponseFactory;
import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.common.ReqRes;
import org.example.socialmediaspring.dto.user.UserRequest;
import org.example.socialmediaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/users")
@Slf4j
@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    ResponseFactory responseFactory;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin:create', 'manager:create')")
    public ResponseEntity createUser(@Valid @RequestBody UserRequest request) {

        return responseFactory.success(userService.createUser(request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:update', 'manager:update')")
    public ResponseEntity updateUser(@Valid @RequestBody UserRequest request, @PathVariable Long id) {
        return responseFactory.success(userService.updateUser(request, id));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyAuthority('admin:delete', 'manager:delete')")
    public ResponseEntity deleteUsersByIds(@Valid @RequestBody LongIdsRequest ids) {

        return responseFactory.success( userService.deleteUsersByIds(ids));

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read')")
    public ResponseEntity getUser(@PathVariable Long id) {
        return responseFactory.success(userService.getUser(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyAuthority('admin:read', 'manager:read')")
    public ResponseEntity findUsersByConds(
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "size", defaultValue = "20", required = false) int size
    ) {
        return responseFactory.success(userService.findUsers(page, size));
    }

    @PostMapping("/change_password")
    public ResponseEntity changePassword(@RequestBody ChangePasswordRequest request, Principal connectedUser){
        return responseFactory.success(userService.changePassword(request, connectedUser));
    }

}
