package com.legmod.ddd.inventory.domain.model.events;

import com.legmod.ddd.inventory.domain.model.aggregates.InventoryId;
import lombok.Getter;

@Getter
public class StockRemovedEvent {
    private final InventoryId inventoryId;
    private final int amount;

    public StockRemovedEvent(InventoryId inventoryId, int amount) {
        this.inventoryId = inventoryId;
        this.amount = amount;
    }

}
