package com.stationery.inventory.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating or updating an inventory item.
 */
public class InventoryRequest {

    @NotBlank(message = "Item code is required")
    private String itemCode;

    @NotBlank(message = "Item name is required")
    private String name;

    private String description;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Minimum quantity threshold is required")
    @Min(value = 0, message = "Threshold cannot be negative")
    private Integer minQuantityThreshold;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price cannot be negative")
    private Double price;

    private String category;

    public InventoryRequest() {
    }

    public InventoryRequest(String itemCode, String name, String description, Integer quantity, 
                            Integer minQuantityThreshold, Double price, String category) {
        this.itemCode = itemCode;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.minQuantityThreshold = minQuantityThreshold;
        this.price = price;
        this.category = category;
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
}
