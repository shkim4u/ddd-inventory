package com.legmod.ddd.inventory.application.queryservices;

import com.legmod.ddd.inventory.domain.model.aggregates.Inventory;
import com.legmod.ddd.inventory.domain.model.aggregates.ProductId;
import com.legmod.ddd.inventory.domain.model.view.InventoryView;
import com.legmod.ddd.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryQueryService {
    private final InventoryRepository inventoryRepository;

    public InventoryView getInventory(String productId) {
        Inventory inventory = inventoryRepository.findByProductId(ProductId.of(productId))
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for productId: " + productId));

        // Convert Inventory to InventoryView.
        return InventoryView.builder()
                .id(inventory.getId().getId())
                .productId(inventory.getProductId().getId())
                .quantity(inventory.getQuantity())
                .price(inventory.getPrice().getValue())
                .build();
    }
}
