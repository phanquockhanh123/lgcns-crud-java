package org.example.socialmediaspring.service;

import org.example.socialmediaspring.common.PageNewResponse;
import org.example.socialmediaspring.common.PageResponse;
import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.book.BookCategoryDto;
import org.example.socialmediaspring.dto.common.IdsRequest;
import org.example.socialmediaspring.dto.common.LongIdsRequest;
import org.example.socialmediaspring.dto.common.ReqRes;
import org.example.socialmediaspring.dto.user.*;
import org.example.socialmediaspring.entity.Role;
import org.example.socialmediaspring.entity.User;

import java.security.Principal;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserRequest request);

    UserResponse updateUser(UserRequest request, Long userId);

    String deleteUsersByIds(LongIdsRequest ids);

    User getUser(Long userId);

    PageNewResponse<UserDto> findUsers(SearchUserRequest request);

    String changePassword(ChangePasswordRequest request, Principal connectedUser);

    PageNewResponse<BestCustomerRes> getUsersReport(SearchUserRequest request);
}
