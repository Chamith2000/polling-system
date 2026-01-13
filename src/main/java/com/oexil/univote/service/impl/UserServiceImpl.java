package com.oexil.univote.service.impl;

import com.oexil.univote.dto.user.UserDTO;
import com.oexil.univote.model.User;
import com.oexil.univote.repository.UserRepository;
import com.oexil.univote.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDTO> searchUsers(String query) {
        List<User> users = userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, query);
        return users.stream().map(user -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setName(user.getFirstName() + " " + user.getLastName());
            userDTO.setEmail(user.getEmail());
            return userDTO;
        }).collect(Collectors.toList());
    }
}
