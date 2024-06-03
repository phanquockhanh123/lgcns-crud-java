package org.example.socialmediaspring.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenBlackListService {
    private Set<String>  blackListedTokens = new HashSet<>();

    public void blacklistToken(String token) {
        blackListedTokens.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blackListedTokens.contains(token);
    }
}
