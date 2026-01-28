package com.oexil.univote.controller;

import com.oexil.univote.dto.user.UserDTO;
import com.oexil.univote.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/search")
    @PreAuthorize("hasRole('USER')")
    @ResponseBody
    public List<UserDTO> searchUsers(@RequestParam("query") String query) {
        return userService.searchUsers(query);
    }
}