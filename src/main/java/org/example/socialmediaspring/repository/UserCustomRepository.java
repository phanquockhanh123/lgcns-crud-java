package org.example.socialmediaspring.repository;

import org.example.socialmediaspring.dto.book.BookResponse;
import org.example.socialmediaspring.dto.book.SearchBookRequest;
import org.example.socialmediaspring.dto.user.BestCustomerRes;
import org.example.socialmediaspring.dto.user.SearchUserRequest;
import org.example.socialmediaspring.dto.user.UserDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.util.Pair;

import java.util.List;

public interface UserCustomRepository {
    Pair<Long, List<UserDto>> getUsersByConds(SearchUserRequest searchReq, Pageable pageable);

    Pair<Long, List<BestCustomerRes>> getCustomersReport(SearchUserRequest searchReq, Pageable pageable);
}
