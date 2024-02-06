package com.chatop.api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chatop.api.model.User;
import com.chatop.api.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public Optional<User> getMe() {
        return userRepository.findById((long) 1);
    }

    public void deleteUser(final Long id) {
        userRepository.deleteById(id);
    }

    public User findByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    public User save (User user) {
        User savedUser = userRepository.save(user);
        return savedUser;
    }
}
