package com.example.offersservice.service;

import com.example.offersservice.model.Item;
import com.example.offersservice.model.ItemResponse;
import com.example.offersservice.model.SaleOfferDto;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@AllArgsConstructor
public class ItemService {

    private final RestTemplate restTemplate;

    public ResponseEntity<ItemResponse> saveItem(Item item) {
        log.info("Preparing and sending REST HTTP request to save item");
        var uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8082)
                .path("/api/v1/items/")
                .build()
                .encode();

        return restTemplate.postForEntity(uriComponents.toUriString(), item, ItemResponse.class);
    }

    public ResponseEntity<ItemResponse> getItem(Long id) {
        log.info("Preparing and sending REST HTTP request to get item");
        var uriComponents = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8082)
                .path("/api/v1/items/{id}")
                .buildAndExpand(id)
                .encode();
        return restTemplate.getForEntity(uriComponents.toUriString(), ItemResponse.class);
    }

    public Item extractItem(SaleOfferDto saleOfferDto) {
        log.info("Extracting item data from sale offer request");
        return new Item(saleOfferDto.getItemName(), saleOfferDto.getItemDescription(),
                saleOfferDto.getItemProducer(), saleOfferDto.getItemCategory());
    }
}
