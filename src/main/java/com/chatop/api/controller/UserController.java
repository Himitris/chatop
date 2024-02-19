package com.chatop.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.api.DTO.LoginRequest;
import com.chatop.api.DTO.RegisterRequest;
import com.chatop.api.model.User;
import com.chatop.api.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SecurityRequirements()
    @PostMapping("/register")
    public ResponseEntity<String> addUser(@RequestBody @NotNull RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.name);
        user.setEmail(registerRequest.email);
        // Encodage du mot de passe avec BCryptPasswordEncoder
        String encodedPassword = bCryptPasswordEncoder().encode(registerRequest.password);
        user.setPassword(encodedPassword);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        user.setCreatedAt(currentDate);
        user.setUpdatedAt(currentDate);
        userService.save(user);
        String token = userService.authenticate(new LoginRequest(registerRequest.email, registerRequest.password));
        return new ResponseEntity<>(token, HttpStatus.CREATED);
    }

    @SecurityRequirements()
    @PostMapping("/login")
    public ResponseEntity<String> getToken(@RequestBody LoginRequest loginRequest) { 
        User user = userService.findByEmail(loginRequest.login);
        if (user != null && bCryptPasswordEncoder().matches(loginRequest.password, user.getPassword())) { 
            String token = userService.authenticate(new LoginRequest(loginRequest.login, loginRequest.password));
            // Création de l'objet JSON
            String responseBody ="{\n  \"token\": \"" + token + "\"\n}";
            return new ResponseEntity<>(responseBody, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/me")
    private ResponseEntity<User> getAuthLoginInfo (Principal user) {
        User userFinded = userService.findByEmail(user.getName());
        return new ResponseEntity<>(userFinded, HttpStatus.CREATED);
    }
}
