package com.legmod.ddd.inventory.interfaces.rest;

import com.legmod.ddd.inventory.application.queryservices.InventoryQueryService;
import com.legmod.ddd.inventory.domain.model.view.InventoryView;
import com.legmod.ddd.inventory.interfaces.rest.dto.AppObject;
import com.legmod.ddd.inventory.interfaces.rest.dto.InventoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/inventory/v1")
@RequiredArgsConstructor
public class InventoryController {
    private final InventoryQueryService inventoryQueryService;

    @GetMapping(value = "/{productId}")
    public ResponseEntity<AppObject<InventoryDto>> getInventory(@PathVariable String productId) {
        InventoryView inventoryView = inventoryQueryService.getInventory(productId);
        InventoryDto inventoryDto = InventoryDto.builder()
                .id(inventoryView.getId())
                .productId(inventoryView.getProductId())
                .quantity(inventoryView.getQuantity())
                .price(inventoryView.getPrice())
                .build();
        AppObject<InventoryDto> appObject = AppObject.appObject(inventoryDto);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(appObject);
    }
}
