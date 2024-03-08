package com.chatop.api.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.api.dto.MessageRequest;
import com.chatop.api.dto.MessageResponse;
import com.chatop.api.model.Message;
import com.chatop.api.model.Rental;
import com.chatop.api.model.User;
import com.chatop.api.service.MessageService;
import com.chatop.api.service.RentalService;
import com.chatop.api.service.UserService;

@RestController
@RequestMapping("/api")
public class MessageController {

    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private RentalService rentalService;


    @PostMapping("/messages")
    public ResponseEntity<?> saveMessage(@RequestBody MessageRequest messageRequest) {
        // Vérifier si l'utilisateur avec l'ID fourni existe dans la table users
        Optional<User> userOptional = userService.getUserById((long) messageRequest.user_id);

        // Vérifier si la location avec l'ID fourni existe dans la table rentals
        Optional<Rental> rentalOptional = rentalService.getRentalById((long) messageRequest.rental_id);

        if (userOptional.isPresent() && rentalOptional.isPresent()) {
            // L'utilisateur et la location existent, vous pouvez continuer avec la création du message
            Message message = new Message();
            message.setMessage(messageRequest.message);
            message.setUser_id(messageRequest.user_id);
            message.setRental_id(messageRequest.rental_id);
            LocalDateTime currentDateTime = LocalDateTime.now();
            Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
            message.setCreated_at(currentDate);
            message.setUpdated_at(currentDate);

            // Enregistrez le message en utilisant le service approprié
            messageService.saveMessage(message);

            // Retournez une réponse avec le statut 201 Created et l'objet Message créé
            return new ResponseEntity<>(new MessageResponse("Message send with success"), HttpStatus.CREATED);
        } else {
            // L'utilisateur ou la location avec l'ID fourni n'existe pas
            return new ResponseEntity<>("User or rental id does not exist", HttpStatus.BAD_REQUEST);
        }
    }
}
