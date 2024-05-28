package org.example.socialmediaspring.service.impl;

import org.apache.catalina.User;
import org.example.socialmediaspring.dto.ApiResponse;
import org.example.socialmediaspring.dto.CreateUserRequest;
import org.example.socialmediaspring.dto.UpdateUserRequest;
import org.example.socialmediaspring.dto.UserResponse;
import org.example.socialmediaspring.entity.UserEntity;
import org.example.socialmediaspring.exception.AppException;
import org.example.socialmediaspring.exception.ErrorCode;
import org.example.socialmediaspring.mapper.UserMapper;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUserNameOrEmail(request.getUserName(), request.getEmail())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        UserEntity user  = UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getUserName())
                .address(request.getAddress())
                .email(request.getEmail())
                .build();

        UserEntity saveUser = userRepository.save(user);

        UserResponse rs = userMapper.toUserResponse(saveUser);

        return rs;
    }

    @Override
    @Transactional
    public UserResponse updateUser(UpdateUserRequest request, Long userId) {

        return userRepository.findById(userId)
                .map(user -> {
                    user.setFirstName(request.getFirstName());
                    user.setLastName(request.getLastName());
                    user.setUserName(request.getUserName());
                    user.setAddress(request.getAddress());
                    user.setEmail(request.getEmail());

                    UserEntity newUser = userRepository.save(user);
                    UserResponse rs = userMapper.toUserResponse(newUser);
                    return rs;
                })
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse getUser(Long userId) {
        return userMapper.toUserResponse(
                userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)));
    }


}