package com.legmod.ddd.inventory.domain.model.view;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InventoryView {
    private String id;
    private String productId;
    private int quantity;
    private int price;
}
