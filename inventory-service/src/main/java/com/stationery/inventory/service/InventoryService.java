package com.stationery.inventory.service;

import com.stationery.inventory.dto.InventoryRequest;
import com.stationery.inventory.dto.InventoryResponse;
import com.stationery.inventory.dto.StockUpdateRequest;

import java.util.List;

/**
 * Service interface defining inventory management actions.
 */
public interface InventoryService {

    /**
     * Creates a new inventory item.
     */
    InventoryResponse createItem(InventoryRequest request);

    /**
     * Retrieves all inventory items.
     */
    List<InventoryResponse> getAllItems();

    /**
     * Retrieves an inventory item by its ID.
     */
    InventoryResponse getItemById(Long id);

    /**
     * Updates an existing inventory item details.
     */
    InventoryResponse updateItem(Long id, InventoryRequest request);

    /**
     * Deletes an inventory item by its ID.
     */
    void deleteItem(Long id);

    /**
     * Updates/Adjusts the stock quantity of an inventory item.
     */
    InventoryResponse updateStock(Long id, StockUpdateRequest request);

    /**
     * Searches inventory items matching a query term.
     */
    List<InventoryResponse> searchInventory(String query);

    /**
     * Retrieves all items that are low in stock.
     */
    List<InventoryResponse> getLowStockItems();
}
