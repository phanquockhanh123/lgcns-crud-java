package org.example.socialmediaspring.service.impl;

import jakarta.persistence.EntityExistsException;
import org.example.socialmediaspring.config.JWTUtils;
import org.example.socialmediaspring.constant.ErrorCodeConst;
import org.example.socialmediaspring.dto.auth.ChangePasswordRequest;
import org.example.socialmediaspring.dto.common.ReqRes;
import org.example.socialmediaspring.dto.emails.EmailDetails;
import org.example.socialmediaspring.entity.Role;
import org.example.socialmediaspring.entity.Token;
import org.example.socialmediaspring.entity.TokenType;
import org.example.socialmediaspring.entity.User;
import org.example.socialmediaspring.exception.BizException;
import org.example.socialmediaspring.repository.TokenRepository;
import org.example.socialmediaspring.repository.UserRepository;
import org.example.socialmediaspring.service.AuthService;
import org.example.socialmediaspring.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.HashMap;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JWTUtils jwtService;
    @Autowired
    EmailService emailService;

    @Override
    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {
            if (userRepository.existsByEmail(registrationRequest.getEmail())) {
                throw new BizException(ErrorCodeConst.INVALID_INPUT, "Email existed");
            }

            User user = new User();
            user.setEmail(registrationRequest.getEmail());
            user.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            user.setRole(Role.USER);
            user.setFirstName(registrationRequest.getFirstName());
            user.setLastName(registrationRequest.getLastName());
            user.setUserName(registrationRequest.getEmail());
            user.setAddress(registrationRequest.getAddress());
            user.setPhone(registrationRequest.getPhone());


            User userResult = userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);


            saveUserToken(userResult, jwtToken);

            if (userResult != null && userResult.getId()>0) {
                resp.setUsers(userResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }

            // send mail to alert user
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(userResult.getEmail())
                    .subject("ACCOUNT CREATION")
                    .messageBody("Welcome to LG. You best!\nYour account Details:\n"
                            + "Account name: " + userResult.getFirstName() + " " + userResult.getLastName()
                            + "\n Email: " + userResult.getEmail())
                    .build();

            emailService.sendEmailAlert(emailDetails);
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    @Override
    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(),signinRequest.getPassword()));
            var user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow();

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
            response.setUsers(user);

            revokeAllUserTokens(user);
            saveUserToken(user, jwt);
        }catch (Exception e){
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    @Override
    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        User users = userRepository.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), users)) {
            var accessToken = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(accessToken);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");

            revokeAllUserTokens(users);
            saveUserToken(users, accessToken);
        }
        response.setStatusCode(200);
        return response;
    }



    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(Math.toIntExact(user.getId()));
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
