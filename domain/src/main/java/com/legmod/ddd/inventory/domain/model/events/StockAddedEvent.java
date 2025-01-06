package com.legmod.ddd.inventory.domain.model.events;

import com.legmod.ddd.inventory.domain.model.aggregates.InventoryId;
import lombok.Getter;

@Getter
public class StockAddedEvent {
    private final InventoryId inventoryId;
    private final int amount;

    public StockAddedEvent(InventoryId inventoryId, int amount) {
        this.inventoryId = inventoryId;
        this.amount = amount;
    }

}
