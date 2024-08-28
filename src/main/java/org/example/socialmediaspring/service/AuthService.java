package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.auth.AuthRes;
import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.common.ReqRes;

import java.security.Principal;

public interface AuthService {
    ReqRes signUp(AuthRes request);

    ReqRes signIn(AuthRes request);

    ReqRes refreshToken(ReqRes request);

}
