package org.example.socialmediaspring.service.impl;

import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.common.ReqRes;
import org.example.socialmediaspring.dto.user.UserRequest;
import org.example.socialmediaspring.dto.user.UserResponse;
import org.example.socialmediaspring.entity.Role;
import org.example.socialmediaspring.entity.User;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.UserMapper;
import org.example.socialmediaspring.repository.TokenRepository;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserResponse createUser(UserRequest request) {
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
                .password(passwordEncoder.encode("123456"))
                .role(Role.USER)
                .build();

        User saveUser = userRepository.save(user);

        UserResponse rs = userMapper.toUserResponse(saveUser);

        return rs;
    }

    @Override
    @Transactional
    public UserResponse updateUser(UserRequest request, Long id) {

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
        existsUser.setRole(Role.valueOf(request.getRole().toUpperCase()));

        User rs =  userRepository.save(existsUser);
        UserResponse rsResponse = userMapper.toUserResponse(rs);

        return rsResponse;
    }

    @Override
    @Transactional
    public String deleteUsersByIds(LongIdsRequest ids) {
        tokenRepository.deleteByUserId(ids.getIds());
        userRepository.deleteAllById(ids.getIds());

        StringBuilder message = new StringBuilder();
        message.append("Delete users ids success");

        return message.toString();
    }

    @Override
    public User getUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "User account not existed");
        }

        return userRepository.findUserById(userId);
    }

    @Override
    public PageResponse<UserResponse> findUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("created").descending());

        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String currentUsername = currentUser.getUsername();

        Page<UserResponse> users = userRepository.findUsersByConds(pageable);

        System.out.println("Result users: {}" + users);
        List<UserResponse> userResponse = users.stream().filter(user -> !user.getUserName().equals(currentUsername))
                .collect(Collectors.toList());
        return new PageResponse<>(
                userResponse,
                users.getNumber(),
                users.getSize(),
                users.getTotalElements(),
                users.getTotalPages(),
                users.isFirst(),
                users.isLast()
        );
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurPass(), currentUser.getPassword())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Wrong password");
        }
        // check if the two new passwords are the same
        if (!request.getNewPass().equals(currentUser.getPassword())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Password no change");
        }

        // check if the two new passwords are the same
        if (!request.getNewPass().equals(request.getConfirmPass())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Password are not the same");
        }

        User user = userRepository.findUsersByUsername(currentUser.getUsername());
        // update the password
        user.setPassword(passwordEncoder.encode(request.getNewPass()));

        // save the new password
         userRepository.save(user);

        return "Change password successful!";
    }
}
