package com.stationery.inventory.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

/**
 * Entity representing an item in the stationery inventory.
 */
@Entity
@Table(name = "inventory_items")
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_code", unique = true, nullable = false)
    private String itemCode;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "min_quantity_threshold", nullable = false)
    private Integer minQuantityThreshold;

    @Column(nullable = false)
    private Double price;

    private String category;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public InventoryItem() {
    }

    public InventoryItem(Long id, String itemCode, String name, String description, Integer quantity, 
                         Integer minQuantityThreshold, Double price, String category, 
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.itemCode = itemCode;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.minQuantityThreshold = minQuantityThreshold;
        this.price = price;
        this.category = category;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
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

    public static InventoryItemBuilder builder() {
        return new InventoryItemBuilder();
    }

    public static class InventoryItemBuilder {
        private Long id;
        private String itemCode;
        private String name;
        private String description;
        private Integer quantity;
        private Integer minQuantityThreshold;
        private Double price;
        private String category;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        InventoryItemBuilder() {
        }

        public InventoryItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public InventoryItemBuilder itemCode(String itemCode) {
            this.itemCode = itemCode;
            return this;
        }

        public InventoryItemBuilder name(String name) {
            this.name = name;
            return this;
        }

        public InventoryItemBuilder description(String description) {
            this.description = description;
            return this;
        }

        public InventoryItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public InventoryItemBuilder minQuantityThreshold(Integer minQuantityThreshold) {
            this.minQuantityThreshold = minQuantityThreshold;
            return this;
        }

        public InventoryItemBuilder price(Double price) {
            this.price = price;
            return this;
        }

        public InventoryItemBuilder category(String category) {
            this.category = category;
            return this;
        }

        public InventoryItemBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public InventoryItemBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public InventoryItem build() {
            return new InventoryItem(id, itemCode, name, description, quantity, minQuantityThreshold, price, category, createdAt, updatedAt);
        }
    }
}
