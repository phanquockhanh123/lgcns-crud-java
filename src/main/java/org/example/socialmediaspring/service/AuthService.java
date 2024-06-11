package org.example.socialmediaspring.service;

import org.example.socialmediaspring.dto.common.ReqRes;

public interface AuthService {
    ReqRes signUp(ReqRes request);

    ReqRes signIn(ReqRes request);

    ReqRes refreshToken(ReqRes request);
}
