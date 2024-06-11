package org.example.socialmediaspring.mapper;

import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User toUserEntity(UserResponse userResponse) {
        if (userResponse == null) {
            return null;
        }

        User user = new User();
        user.setId(userResponse.getId());
        user.setFirstName(userResponse.getFirstName());
        user.setLastName(userResponse.getLastName());
        user.setUserName(userResponse.getUserName());
        user.setAddress(userResponse.getAddress());
        user.setEmail(userResponse.getEmail());
        user.setPhone(userResponse.getPhone());
        return user;
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) {
            return null;
        }

        UserResponse userRes = new UserResponse();
        userRes.setId(user.getId());
        userRes.setFirstName(user.getFirstName());
        userRes.setLastName(user.getLastName());
        userRes.setUserName(user.getUsername());
        userRes.setAddress(user.getAddress());
        userRes.setEmail(user.getEmail());
        userRes.setPhone(user.getPhone());
        userRes.setRole(user.getRole());

        return userRes;
    }

}
