package com.chatop.api.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.chatop.api.DTO.RentalRequest;
import com.chatop.api.model.Rental;
import com.chatop.api.model.User;
import com.chatop.api.service.RentalService;
import com.chatop.api.service.StorageService;

import io.swagger.v3.oas.annotations.Parameter;


@RestController
@RequestMapping("/api")
public class RentalController {
    @Autowired
    private RentalService rentalService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/rentals")
    public Iterable<Rental> getRentals() {
        return rentalService.getRentals();
    }

    @GetMapping("/rentals/{id}")
    public Optional<Rental> getRentalById(@PathVariable Long id) {
        return rentalService.getRentalById(id);
    }

    @PostMapping(value = "/rentals/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Rental> createRental(
            @Parameter(hidden = true) @RequestParam(required = false) @PathVariable Long id,
            MultipartFile picture,
            RentalRequest rentalRequest) throws IOException {

        // Récupérez l'utilisateur courant à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Long currentUserId = currentUser.getId();

        // Créez un nouvel objet Rental avec les paramètres fournis
        Rental newRental = new Rental();
        newRental.setId(id);
        newRental.setName(rentalRequest.name);
        newRental.setSurface(rentalRequest.surface);
        newRental.setPrice(rentalRequest.price);
        if (picture != null && !picture.isEmpty()) {
            String fileUrl = storageService.save(picture);
            newRental.setPicture(fileUrl);
        } else {
            newRental.setPicture("noPicture");
        }
        newRental.setDescription(rentalRequest.description);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        newRental.setCreatedAt(currentDate);
        newRental.setUpdatedAt(currentDate);
        newRental.setOwnerId(currentUserId);

        // Enregistrez le nouvel objet Rental en utilisant le service
        Rental createdRental = rentalService.saveRental(newRental);

        // Retournez une réponse avec le statut 201 Created et l'objet Rental créé
        return new ResponseEntity<>(createdRental, HttpStatus.CREATED);
    }


    @PutMapping("/rentals/{id}")
    public ResponseEntity<Rental> updateRentalById(@PathVariable Long id, RentalRequest rentalRequest) {
        // Récupérez le Rental existant à partir de la base de données
        Optional<Rental> existingRentalOpt = rentalService.getRentalById(id);
        if (!existingRentalOpt.isPresent()) {
            // Si le Rental n'existe pas, retournez une réponse avec le statut 404 Not Found
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Mettez à jour les informations du Rental existant avec les nouvelles informations
        Rental existingRental = existingRentalOpt.get();
        existingRental.setName(rentalRequest.name);
        existingRental.setSurface(rentalRequest.surface);
        existingRental.setPrice(rentalRequest.price);
        existingRental.setDescription(rentalRequest.description);
        LocalDateTime currentDateTime = LocalDateTime.now();
        Date currentDate = Date.from(currentDateTime.atZone(ZoneId.systemDefault()).toInstant());
        existingRental.setUpdatedAt(currentDate);

        // Enregistrez le Rental mis à jour en utilisant le service
        Rental updatedRental = rentalService.saveRental(existingRental);

        // Retournez une réponse avec le statut 200 OK et le Rental mis à jour
        return new ResponseEntity<>(updatedRental, HttpStatus.OK);
    }
}
