package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.CreateUserRequest;
import org.example.socialmediaspring.dto.ReqRes;

public interface AuthService {
    ReqRes signUp(ReqRes request);

    ReqRes signIn(ReqRes request);

    ReqRes refreshToken(ReqRes request);
}
