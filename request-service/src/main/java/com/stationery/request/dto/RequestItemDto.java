package com.stationery.request.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing an item detail inside a request payload.
 */
public class RequestItemDto {

    @NotNull(message = "Inventory Item ID is required")
    private Long inventoryItemId;

    @NotBlank(message = "Item name is required")
    private String itemName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public RequestItemDto() {
    }

    public RequestItemDto(Long inventoryItemId, String itemName, Integer quantity) {
        this.inventoryItemId = inventoryItemId;
        this.itemName = itemName;
        this.quantity = quantity;
    }

    public Long getInventoryItemId() {
        return inventoryItemId;
    }

    public void setInventoryItemId(Long inventoryItemId) {
        this.inventoryItemId = inventoryItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
