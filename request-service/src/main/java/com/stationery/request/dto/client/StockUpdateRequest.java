package com.stationery.request.dto.client;

/**
 * Integration DTO representing stock update request sent to Inventory Service.
 */
public class StockUpdateRequest {

    private Integer amount;
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
