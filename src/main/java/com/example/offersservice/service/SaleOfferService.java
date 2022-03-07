package com.example.offersservice.service;

import com.example.offersservice.model.*;
import com.example.offersservice.repository.SaleOfferRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class SaleOfferService {

    private final SaleOfferRepository saleOfferRepository;
    private final ItemService itemService;

    public SaleOffer createSaleOffer(SaleOfferDto saleOfferDto) {
        log.info("Creating sale offer");
        var item = itemService.extractItem(saleOfferDto);
        ResponseEntity<ItemResponse> itemResponse = itemService.saveItem(item);
        SaleOffer saleOffer;

        if (itemResponse.getStatusCode()
                .is2xxSuccessful() && Objects.nonNull(itemResponse.getBody())) {
            saleOffer = buildSaleOffer(itemResponse.getBody()
                    .getId(), saleOfferDto);
        } else {
            saleOffer = buildSaleOffer(null, saleOfferDto);
        }

        return saleOfferRepository.save(saleOffer);
    }

    public Optional<SaleOffer> retrieveSaleOffer(Long id) {
        log.info("Retrieving sale offer with id {}", id);
        return saleOfferRepository.findById(id);
    }

    public SaleOfferDto retrieveSaleOfferWithDetails(Long id) {
        log.info("Retrieving sale offer with details for id {}", id);
        Optional<SaleOffer> saleOffer = saleOfferRepository.findById(id);

        if (saleOffer.isPresent()) {
            var itemResponse = itemService.getItem(saleOffer.get()
                    .getItemId());
            var saleOfferDtoBuilder = SaleOfferDto.builder()
                    .name(saleOffer.get()
                            .getName())
                    .description(saleOffer.get()
                            .getDescription())
                    .isNew(saleOffer.get()
                            .isNew())
                    .price(saleOffer.get()
                            .getPrice())
                    .quantity(saleOffer.get()
                            .getQuantity())
                    .status(saleOffer.get()
                            .getStatus())
                    .shipmentNames(saleOffer.get()
                            .getShipmentNames())
                    .paymentNames(saleOffer.get()
                            .getPaymentNames());

            if (itemResponse.getStatusCode()
                    .is2xxSuccessful() && Objects.nonNull(itemResponse.getBody())) {
                saleOfferDtoBuilder.itemName(itemResponse.getBody()
                                .getName())
                        .itemDescription(itemResponse.getBody()
                                .getDescription())
                        .itemProducer(itemResponse.getBody()
                                .getProducer())
                        .itemCategory(itemResponse.getBody()
                                .getCategory())
                        .build();
            }

            return saleOfferDtoBuilder.build();
        } else {
            return null;
        }
    }

    public List<SaleOffer> retrieveAllSaleOffers() {
        log.info("Retrieving all sale offers");
        return saleOfferRepository.findAll();
    }

    public void deleteSaleOffer(Long id) {
        log.info("Delete sale offer with id {} from database", id);
        saleOfferRepository.deleteById(id);
    }

    public SaleOffer updateSaleOffer(Long id, SaleOfferMain saleOfferMain) {
        log.info("Updating sale offer with id {}", id);
        Optional<SaleOffer> oldSafeOffer = retrieveSaleOffer(id);

        if (oldSafeOffer.isPresent()) {
            oldSafeOffer.get()
                    .setName(saleOfferMain.getName());
            oldSafeOffer.get()
                    .setDescription(saleOfferMain.getDescription());
            oldSafeOffer.get()
                    .setPrice(saleOfferMain.getPrice());
            oldSafeOffer.get()
                    .setQuantity(saleOfferMain.getQuantity());
            oldSafeOffer.get()
                    .setNew(saleOfferMain.isNew());
            oldSafeOffer.get()
                    .setPaymentNames(saleOfferMain.getPaymentNames());
            oldSafeOffer.get()
                    .setShipmentNames(saleOfferMain.getShipmentNames());
            return saleOfferRepository.save(oldSafeOffer.get());
        } else {
            return null;
        }
    }

    public SaleOffer reduceQuantityForOffer(Long id, Long quantityToReduce) {
        log.info("Reducing quantity of {} for sale offer with id {}", quantityToReduce, id);
        Optional<SaleOffer> oldSafeOffer = retrieveSaleOffer(id);

        if (oldSafeOffer.isPresent()) {
            var oldQuantity = oldSafeOffer.get()
                    .getQuantity();
            var newQuantity = oldQuantity - quantityToReduce;

            oldSafeOffer.get()
                    .setQuantity(newQuantity);
            return saleOfferRepository.save(oldSafeOffer.get());
        } else {
            return null;
        }
    }

    private SaleOffer buildSaleOffer(Long itemId, SaleOfferDto saleOfferDto) {
        return new SaleOffer(itemId, saleOfferDto.getName(), saleOfferDto.getDescription(),
                saleOfferDto.isNew(), saleOfferDto.getPrice(), saleOfferDto.getQuantity(),
                saleOfferDto.getStatus(), saleOfferDto.getShipmentNames(), saleOfferDto.getPaymentNames());
    }

}
