package com.example.offersservice.repository;

import com.example.offersservice.model.SaleOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOfferRepository extends JpaRepository<SaleOffer, Long> {
}
