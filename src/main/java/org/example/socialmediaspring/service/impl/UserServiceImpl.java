package org.example.socialmediaspring.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.constant.Common;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.book.BookBestSellerRes;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.common.ReqRes;
import org.example.socialmediaspring.dto.user.*;
import org.example.socialmediaspring.entity.Role;
import org.example.socialmediaspring.entity.User;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.mapper.UserMapper;
import org.example.socialmediaspring.repository.TokenRepository;
import org.example.socialmediaspring.repository.UserCustomRepository;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.UserService;
import org.example.socialmediaspring.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserCustomRepository userCustomRepository;


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
    public PageNewResponse<UserDto> findUsers(SearchUserRequest searchReq) {
        log.info("start search all books. body: {}", JsonUtils.objToString(searchReq));
        PageRequest pageable = Common.getPageRequest(searchReq.getPage() - 1, searchReq.getLimit(), null);

        Pair<Long, List<UserDto>> usersData = userCustomRepository.getUsersByConds(searchReq, pageable);
        Long countBooks = usersData.getFirst();
        List<UserDto> listUsers = usersData.getSecond();

        Page<UserDto> pageUserDto = new PageImpl<>(listUsers, pageable, countBooks);

        PageNewResponse<UserDto> ib = PageNewResponse.<UserDto>builder()
                .data(listUsers)
                .build();

        if (Objects.nonNull(searchReq.getGetTotalCount()) && Boolean.TRUE.equals(searchReq.getGetTotalCount())) {
            ib.setPagination(this.buildPagination(pageUserDto.getSize(), pageUserDto.getTotalPages(),
                    pageUserDto.getNumber() + 1, pageUserDto.getTotalElements()));
        }

        log.info("end ...");
        return ib;
    }

    @Override
    public String changePassword(ChangePasswordRequest request, Principal connectedUser) {
        UserDetails currentUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // check if the current password is correct
        if (!passwordEncoder.matches(request.getCurPass(), currentUser.getPassword())) {
            throw new BizException(ErrorCodeConst.INVALID_INPUT, "Wrong password");
        }
        // check if the two new passwords are the same
        if (request.getNewPass().equals(currentUser.getPassword())) {
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

    private Map<String, Long> buildPagination(Integer limit, Integer totalPage, Integer currentPage, Long totalRecord){
        log.info("start buildPagination ...");

        Map<String, Long> pagination = new HashMap<>();
        pagination.put("limit", Long.valueOf(limit));
        pagination.put("total_page", Long.valueOf(totalPage));
        pagination.put("current_page", Long.valueOf(currentPage));
        pagination.put("total_record", totalRecord);

        log.info("end buildPagination ...");
        return pagination;
    }

    @Override
    public PageNewResponse<BestCustomerRes> getUsersReport(SearchUserRequest searchReq) {
        log.info("start search users report. body: {}", JsonUtils.objToString(searchReq));
        PageRequest pageable = Common.getPageRequest(searchReq.getPage() - 1, searchReq.getLimit(), null);

        Pair<Long, List<BestCustomerRes>> users = userCustomRepository.getCustomersReport(searchReq, pageable);
        Long countUsers = users.getFirst();
        List<BestCustomerRes> listUsers = users.getSecond();

        Page<BestCustomerRes> pageUserDto = new PageImpl<>(listUsers, pageable, countUsers);

        PageNewResponse<BestCustomerRes> ib = PageNewResponse.<BestCustomerRes>builder()
                .data(listUsers)
                .build();

        if (Objects.nonNull(searchReq.getGetTotalCount()) && Boolean.TRUE.equals(searchReq.getGetTotalCount())) {
            ib.setPagination(this.buildPagination(pageUserDto.getSize(), pageUserDto.getTotalPages(),
                    pageUserDto.getNumber() + 1, pageUserDto.getTotalElements()));
        }

        log.info("end ...");
        return ib;
    }
}
