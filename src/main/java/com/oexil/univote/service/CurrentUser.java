package com.oexil.univote.service;

import com.oexil.univote.model.User;
import org.springframework.stereotype.Service;

@Service
public interface CurrentUser {
    User getUser();

    User getUserForGlobalControllerAdvice();
}
