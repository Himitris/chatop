package com.chatop.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.api.dto.LoginRequest;
import com.chatop.api.dto.LoginResponse;
import com.chatop.api.dto.RegisterRequest;
import com.chatop.api.model.User;
import com.chatop.api.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Optional;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;
    
    @Autowired
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @SecurityRequirements()
    @PostMapping("/auth/register")
    public ResponseEntity<?> addUser(@RequestBody @NotNull RegisterRequest registerRequest) {
        if (userService.findByEmail(registerRequest.email) != null) {
            return new ResponseEntity<>("User with the same email already exists", HttpStatus.BAD_REQUEST);
        }else {
            User user = new User();
            user.setName(registerRequest.name);
            user.setEmail(registerRequest.email);
            // Encodage du mot de passe avec BCryptPasswordEncoder
            String encodedPassword = bCryptPasswordEncoder().encode(registerRequest.password);
            user.setPassword(encodedPassword);
            LocalDateTime currentDateTime = LocalDateTime.now();
            Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
            user.setCreated_at(currentDate);
            user.setUpdated_at(currentDate);        
            userService.save(user);
            LoginResponse response = new LoginResponse(userService.authenticate(new LoginRequest(registerRequest.email, registerRequest.password)));

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        
    }

    @SecurityRequirements()
    @PostMapping("/auth/login")
    public ResponseEntity<?> getToken(@RequestBody LoginRequest loginRequest) { 
        User user = userService.findByEmail(loginRequest.email);
        if (user != null && bCryptPasswordEncoder().matches(loginRequest.password, user.getPassword())) { 
            return new ResponseEntity<>(new LoginResponse(userService.authenticate(new LoginRequest(loginRequest.email, loginRequest.password))), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/auth/me")
    private ResponseEntity<User> getAuthLoginInfo (Principal user) {
        return new ResponseEntity<>(userService.findByEmail(user.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/user/{id}")
    private ResponseEntity<Optional<User>> getUserById (@PathVariable Long id) {
        Optional<User> getUserById = userService.findById(id);
        if (getUserById != null){
            return  new ResponseEntity<>(getUserById, HttpStatus.CREATED);
        } else {
            return null;
        }
        
    }
}
