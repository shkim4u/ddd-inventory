package com.legmod.ddd.inventory.domain.model.aggregates;

import com.legmod.ddd.inventory.domain.model.events.StockAddedEvent;
import com.legmod.ddd.inventory.domain.model.events.StockRemovedEvent;
import com.legmod.ddd.inventory.domain.model.valueobjects.Money;
import com.legmod.ddd.inventory.domain.model.valueobjects.converter.MoneyConverter;
import lombok.Getter;
import org.springframework.data.domain.AbstractAggregateRoot;

import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "inventory")
public class Inventory extends AbstractAggregateRoot<Inventory> {
    @EmbeddedId
    private InventoryId id;

    @Column(name = "product_id")
    private ProductId productId;

    @Column(name = "quantity")
    private int quantity;

    @Convert(converter = MoneyConverter.class)
    @Column(name = "price")
    private Money price;

    protected Inventory() {
    }

    public Inventory(InventoryId id, ProductId productId, int quantity, Money price) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }

    public void addStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        this.quantity += amount;
        registerEvent(new StockAddedEvent(id, amount));
    }

    public void removeStock(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }
        if (this.quantity < amount) {
            throw new IllegalArgumentException("Not enough stock to remove");
        }
        this.quantity -= amount;
        registerEvent(new StockRemovedEvent(id, amount));
    }
}
