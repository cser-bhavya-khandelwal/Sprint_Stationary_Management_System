package com.stationery.request.dto.client;

/**
 * Integration DTO representing inventory item response from Inventory Service.
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
    private Boolean lowStock;

    public InventoryResponse() {
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

    public Boolean getLowStock() {
        return lowStock;
    }

    public void setLowStock(Boolean lowStock) {
        this.lowStock = lowStock;
    }
}
