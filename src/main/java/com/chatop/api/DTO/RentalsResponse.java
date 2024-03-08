package com.chatop.api.dto;


import java.util.ArrayList;
import java.util.List;

import com.chatop.api.model.Rental;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalsResponse {
    public List<Rental> rentals;

    public RentalsResponse(Iterable<Rental> rentalsIterable) {
        this.rentals = new ArrayList<>();
        rentalsIterable.forEach(this.rentals::add);
    }
}

