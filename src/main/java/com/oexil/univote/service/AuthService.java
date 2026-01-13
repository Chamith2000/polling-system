package com.oexil.univote.service;

import com.oexil.univote.dto.user.UserAuth;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<?> authenticateUser(UserAuth login);
}
