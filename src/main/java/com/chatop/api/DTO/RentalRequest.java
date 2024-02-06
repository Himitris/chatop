package com.chatop.api.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalRequest {
    public String name;
    public int surface;
    public int price;
    public String description;
}
