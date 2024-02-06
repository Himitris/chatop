package com.chatop.api.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.chatop.api.DTO.MessageRequest;
import com.chatop.api.model.Message;
import com.chatop.api.service.MessageService;

@RestController
public class MessageControler {

    @Autowired
    private MessageService messageService;

    @PostMapping("/messages")
    public Message saveMessage(@RequestBody MessageRequest messageRequest) {
        Message message = new Message();
        message.setMessage(messageRequest.message);
        message.setUserId(messageRequest.user_id);
        message.setRentalId(messageRequest.rental_id);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        message.setCreatedAt(currentDate);
        message.setUpdatedAt(currentDate);
        return messageService.saveMessage(message);
    }
}
