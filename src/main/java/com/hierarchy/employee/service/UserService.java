package com.hierarchy.employee.service;

import com.hierarchy.employee.dto.UserDto;
import com.hierarchy.employee.model.User;
import com.hierarchy.employee.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public UUID addUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setId(null);

        User createdUser = userRepository.saveAndFlush(user);

        return createdUser.getId();
    }

    public UserDto getUserByEmail(String email) {
        User byEmail = userRepository.findByEmail(email);

        if(Objects.isNull(byEmail)) {
            throw new RuntimeException("user not exist " + email);
        }

        return modelMapper.map(byEmail, UserDto.class);
    }
}
