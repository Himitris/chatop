package com.chatop.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.chatop.api.dto.LoginRequest;
import com.chatop.api.model.User;
import com.chatop.api.repository.UserRepository;
import com.chatop.api.security.JwtTokenProvider;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    public User getMe() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(name);
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User save (User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }

    public String authenticate(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationCredentialsNotFoundException ex) {
            throw new AuthenticationCredentialsNotFoundException("Authentication not permitted");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    public Optional<User> getUserById(final Long id) {
        return userRepository.findById(id);
    }

}
