package com.stationery.inventory.repository;

import com.stationery.inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing InventoryItem persistence.
 */
@Repository
public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {

    /**
     * Finds an inventory item by its unique item code.
     */
    Optional<InventoryItem> findByItemCode(String itemCode);

    /**
     * Checks if an inventory item exists with the given item code.
     */
    boolean existsByItemCode(String itemCode);

    /**
     * Retrieves all items that have stock quantity less than or equal to their minimum threshold.
     */
    @Query("SELECT i FROM InventoryItem i WHERE i.quantity <= i.minQuantityThreshold")
    List<InventoryItem> findLowStockItems();

    /**
     * Searches for inventory items matching name, category, or item code.
     */
    @Query("SELECT i FROM InventoryItem i WHERE " +
            "LOWER(i.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.category) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(i.itemCode) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<InventoryItem> searchInventory(@Param("query") String query);
}
