package com.chatop.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.api.DTO.LoginRequest;
import com.chatop.api.DTO.RegisterRequest;
import com.chatop.api.configuration.SpringSecurityConfig;
import com.chatop.api.model.User;
import com.chatop.api.service.JWTService;
import com.chatop.api.service.UserService;

import java.util.Date;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
@RequestMapping("/auth")
public class UserController {

    public JWTService jwtService;

    public UserController(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Autowired
    private UserService userService;
    
    @Autowired
    private SpringSecurityConfig springSecurityConfig;

    @PostMapping("/register")
    public @ResponseBody String addUser(@RequestBody RegisterRequest registerRequest) {
        User user = new User();
        user.setName(registerRequest.name);
        user.setEmail(registerRequest.email);
        // Encodage du mot de passe avec BCryptPasswordEncoder
        String encodedPassword = springSecurityConfig.passwordEncoder().encode(registerRequest.password);
        user.setPassword(encodedPassword);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        user.setCreatedAt(currentDate);
        user.setUpdatedAt(currentDate);
        userService.save(user);
        return "Inscription réalisée avec succès pour l'utilisateur avec l'ID : ";
    }

    @PostMapping("/login")
    public String getToken(@RequestBody LoginRequest loginRequest) { 
        User user = userService.findByEmail(loginRequest.login);
        if (user != null && springSecurityConfig.passwordEncoder().matches(loginRequest.password, user.getPassword())) { 
            // Générer et retourner un token (peut être implémenté en fonction de vos besoins)
            String token = jwtService.generateToken(loginRequest);
            return token;
        } else {
            return "Échec de la connexion. Vérifiez vos informations d'identification.";
        }
    }

    @GetMapping("/test")
    public String getuserInfo(Principal user) {
        StringBuffer userInfo = new StringBuffer();
        if (user instanceof UsernamePasswordAuthenticationToken) {
            userInfo.append(getAuthLoginInfo(user));
        }
        return userInfo.toString();
    }

    
    private StringBuffer getAuthLoginInfo (Principal user) {
        StringBuffer emailInfo = new StringBuffer();
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) user;
        if(token.isAuthenticated()){
            User u = (User) token.getPrincipal();
            emailInfo.append("Welcome, " + u.getEmail());
        } else {
            emailInfo.append("NA");
        }
        return emailInfo;
    }

    @GetMapping
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(userPrincipal);
    }

}
