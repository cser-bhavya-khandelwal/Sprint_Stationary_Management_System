package com.stationery.inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing a request to update inventory stock.
 */
public class StockUpdateRequest {

    @NotNull(message = "Amount is required")
    private Integer amount;

    @NotBlank(message = "Action (ADD, REDUCE, SET) is required")
    private String action;

    public StockUpdateRequest() {
    }

    public StockUpdateRequest(Integer amount, String action) {
        this.amount = amount;
        this.action = action;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
