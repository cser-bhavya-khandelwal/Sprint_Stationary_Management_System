package com.stationery.inventory.dto;

import java.time.LocalDateTime;

/**
 * DTO representing an inventory item response.
 */
public class InventoryResponse {

    private Long id;
    private String itemCode;
    private String name;
    private String description;
    private Integer quantity;
    private Integer minQuantityThreshold;
    private Double price;
    private String category;
    private boolean lowStock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public InventoryResponse() {
    }

    public InventoryResponse(Long id, String itemCode, String name, String description, Integer quantity, 
                             Integer minQuantityThreshold, Double price, String category, boolean lowStock, 
                             LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.itemCode = itemCode;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.minQuantityThreshold = minQuantityThreshold;
        this.price = price;
        this.category = category;
        this.lowStock = lowStock;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getMinQuantityThreshold() {
        return minQuantityThreshold;
    }

    public void setMinQuantityThreshold(Integer minQuantityThreshold) {
        this.minQuantityThreshold = minQuantityThreshold;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isLowStock() {
        return lowStock;
    }

    public void setLowStock(boolean lowStock) {
        this.lowStock = lowStock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
