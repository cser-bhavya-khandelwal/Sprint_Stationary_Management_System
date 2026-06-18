package com.stationery.request.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO representing status update request.
 */
public class RequestStatusUpdateDto {

    @NotBlank(message = "Status is required")
    private String status;

    public RequestStatusUpdateDto() {
    }

    public RequestStatusUpdateDto(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
