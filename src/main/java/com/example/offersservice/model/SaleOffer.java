package com.example.offersservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "SALE_OFFERS")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SaleOffer extends BaseEntity {

    private Long itemId;

    private String name;
    private String description;
    private boolean isNew;
    private Double price;
    private Long quantity;
    private String status;

    @ElementCollection
    public List<String> shipmentNames;
    @ElementCollection
    public List<String> paymentNames;

}
