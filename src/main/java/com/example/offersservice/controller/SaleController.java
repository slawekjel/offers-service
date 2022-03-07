package com.example.offersservice.controller;

import com.example.offersservice.model.SaleOffer;
import com.example.offersservice.model.SaleOfferMain;
import com.example.offersservice.model.SaleOfferDto;
import com.example.offersservice.service.SaleOfferService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("api/v1/offers/sales/")
public class SaleController {

    private SaleOfferService saleOfferService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleOffer> createSaleOffer(@RequestBody SaleOfferDto saleOfferDto) {
        log.info("Received a request with sale offer to create new sale offer.");
        try {
            var saleOffer = saleOfferService.createSaleOffer(saleOfferDto);
            return new ResponseEntity<>(saleOffer, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleOffer> updateSaleOffer(@PathVariable Long id, @RequestBody SaleOfferMain saleOfferMain) {
        log.info("Received a request to delete sale offer.");
        var saleOffer = saleOfferService.updateSaleOffer(id, saleOfferMain);
        if (Objects.nonNull(saleOffer)) {
            return new ResponseEntity<>(saleOffer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, params = "quantityToReduce")
    public ResponseEntity<SaleOffer> reduceQuantityOfItemsInSaleOffer(@PathVariable Long id, @RequestParam Long quantityToReduce) {
        log.info("Received a request to reduce quantity of items for offer.");
        var saleOffer = saleOfferService.reduceQuantityForOffer(id, quantityToReduce);
        if (Objects.nonNull(saleOffer)) {
            return new ResponseEntity<>(saleOffer, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<SaleOffer>> getAllSaleOffers() {
        log.info("Received a request retrieve all sale offers.");
        try {
            List<SaleOffer> saleOffers = saleOfferService.retrieveAllSaleOffers();

            if (saleOffers.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(saleOffers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleOffer> getSaleOffer(@PathVariable Long id) {
        log.info("Received a request to retrieve sale offer.");
        Optional<SaleOffer> saleOffer = saleOfferService.retrieveSaleOffer(id);
        return saleOffer.map(offer -> new ResponseEntity<>(offer, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NO_CONTENT));
    }

    @GetMapping(path = "{id}/details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SaleOfferDto> getSaleOfferWithDetails(@PathVariable Long id) {
        log.info("Received a request to retrieve sale offer.");
        var saleOfferDto = saleOfferService.retrieveSaleOfferWithDetails(id);

        return Objects.nonNull(saleOfferDto)
                ? new ResponseEntity<>(saleOfferDto, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping(path = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<HttpStatus> deleteSaleOffer(@PathVariable Long id) {
        log.info("Received a request to delete sale offer.");
        try {
            saleOfferService.deleteSaleOffer(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
