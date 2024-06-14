package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.common.ReqRes;

import java.security.Principal;

public interface AuthService {
    ReqRes signUp(ReqRes request);

    ReqRes signIn(ReqRes request);

    ReqRes refreshToken(ReqRes request);

}
