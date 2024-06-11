package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.user.CreateUserRequest;
import org.example.socialmediaspring.dto.user.UpdateUserRequest;
import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.entity.Book;
import org.example.socialmediaspring.entity.User;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.UserMapper;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        if (userRepository.existsByUserNameOrEmail(request.getEmail(), request.getEmail())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Email existed");
        }

        User user  = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .userName(request.getEmail())
                .address(request.getAddress())
                .email(request.getEmail())
                .userName(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role("USER")
                .build();

        User saveUser = userRepository.save(user);

        UserResponse rs = userMapper.toUserResponse(saveUser);

        return rs;
    }

    @Override
    @Transactional
    public UserResponse updateUser(UpdateUserRequest request, Long id) {

        User existsUser = userRepository.findById(id)
                .orElseThrow(() -> new BizException(ErrorCodeConst.INVALID_INPUT, "User not found with id " + id));

        // check another title same exists
        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            if (!user.getId().equals(id)) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT,"User with email " + request.getEmail() + " already exists");
            }
        });

        existsUser.setFirstName(request.getFirstName());
        existsUser.setLastName(request.getLastName());
        existsUser.setEmail(request.getEmail());
        existsUser.setPhone(request.getPhone());
        existsUser.setAddress(request.getAddress());
        existsUser.setRole(request.getRole());

        User rs =  userRepository.save(existsUser);
        UserResponse rsResponse = userMapper.toUserResponse(rs);

        return rsResponse;
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "User not existed");
        }

        userRepository.deleteById(userId);
    }

    @Override
    public User getUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "User account not existed");
        }

        return userRepository.findUserById(userId);
    }

}
