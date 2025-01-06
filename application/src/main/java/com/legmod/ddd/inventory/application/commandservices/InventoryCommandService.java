package com.legmod.ddd.inventory.application.commandservices;

import com.legmod.ddd.inventory.domain.model.aggregates.Inventory;
import com.legmod.ddd.inventory.domain.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryCommandService {
    private final InventoryRepository inventoryRepository;

    public InventoryCommandService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    // TODO: Implement the removeStock method upon receiving an OrderPlacedEvent from RabbitMQ.
//    @Transactional
//    public void removeStock(OrderPlacedEvent event) {
//        event.getOrderLines().forEach(orderLine -> {
//            Inventory inventory = inventoryRepository.findByProductId(orderLine.getProductId())
//                    .orElseThrow(() -> new IllegalArgumentException("No inventory found for product: " + orderLine.getProductId()));
//            inventory.removeStock(orderLine.getQuantity());
//            inventoryRepository.save(inventory);
//        });
//    }
}
