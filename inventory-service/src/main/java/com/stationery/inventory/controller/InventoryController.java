package com.stationery.inventory.controller;

import com.stationery.inventory.dto.InventoryRequest;
import com.stationery.inventory.dto.InventoryResponse;
import com.stationery.inventory.dto.StockUpdateRequest;
import com.stationery.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller providing REST APIs for inventory management.
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * POST /api/inventory : Create a new inventory item.
     */
    @PostMapping
    public ResponseEntity<InventoryResponse> createItem(@Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.createItem(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/inventory : Retrieve all inventory items.
     */
    @GetMapping
    public ResponseEntity<List<InventoryResponse>> getAllItems() {
        List<InventoryResponse> response = inventoryService.getAllItems();
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventory/{id} : Retrieve inventory item by ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InventoryResponse> getItemById(@PathVariable Long id) {
        InventoryResponse response = inventoryService.getItemById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/inventory/{id} : Update an inventory item.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InventoryResponse> updateItem(@PathVariable Long id, @Valid @RequestBody InventoryRequest request) {
        InventoryResponse response = inventoryService.updateItem(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/inventory/{id} : Delete an inventory item.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        inventoryService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * PATCH /api/inventory/{id}/stock : Adjust stock quantity.
     */
    @PatchMapping("/{id}/stock")
    public ResponseEntity<InventoryResponse> updateStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest request) {
        InventoryResponse response = inventoryService.updateStock(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventory/search : Search inventory items matching the query parameter.
     */
    @GetMapping("/search")
    public ResponseEntity<List<InventoryResponse>> searchInventory(@RequestParam String query) {
        List<InventoryResponse> response = inventoryService.searchInventory(query);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/inventory/low-stock : Retrieve all low-stock items.
     */
    @GetMapping("/low-stock")
    public ResponseEntity<List<InventoryResponse>> getLowStockItems() {
        List<InventoryResponse> response = inventoryService.getLowStockItems();
        return ResponseEntity.ok(response);
    }
}
