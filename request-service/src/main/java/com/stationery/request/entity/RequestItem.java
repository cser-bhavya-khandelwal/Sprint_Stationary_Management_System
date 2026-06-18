package com.stationery.request.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Entity representing an item detail inside a stationery request.
 */
@Entity
@Table(name = "request_items")
public class RequestItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "inventory_item_id", nullable = false)
    private Long inventoryItemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id", nullable = false)
    @JsonIgnore
    private Request request;

    public RequestItem() {
    }

    public RequestItem(Long id, Long inventoryItemId, String itemName, Integer quantity, Request request) {
        this.id = id;
        this.inventoryItemId = inventoryItemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.request = request;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public static RequestItemBuilder builder() {
        return new RequestItemBuilder();
    }

    public static class RequestItemBuilder {
        private Long id;
        private Long inventoryItemId;
        private String itemName;
        private Integer quantity;
        private Request request;

        RequestItemBuilder() {
        }

        public RequestItemBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RequestItemBuilder inventoryItemId(Long inventoryItemId) {
            this.inventoryItemId = inventoryItemId;
            return this;
        }

        public RequestItemBuilder itemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public RequestItemBuilder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        public RequestItemBuilder request(Request request) {
            this.request = request;
            return this;
        }

        public RequestItem build() {
            return new RequestItem(id, inventoryItemId, itemName, quantity, request);
        }
    }
}
