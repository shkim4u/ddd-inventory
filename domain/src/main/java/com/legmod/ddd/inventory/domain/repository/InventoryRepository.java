package com.legmod.ddd.inventory.domain.repository;

import com.legmod.ddd.inventory.domain.model.aggregates.Inventory;
import com.legmod.ddd.inventory.domain.model.aggregates.InventoryId;
import com.legmod.ddd.inventory.domain.model.aggregates.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<Inventory, InventoryId> {
    Optional<Inventory> findByProductId(ProductId productId);
}
