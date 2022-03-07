package com.example.offersservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {

    private String name;
    private String description;
    private String producer;
    private String category;
}
