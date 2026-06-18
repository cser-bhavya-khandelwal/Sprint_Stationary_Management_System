package com.stationery.request.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * DTO representing request creation payload.
 */
public class RequestCreateDto {

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotEmpty(message = "Request must contain at least one item")
    @Valid
    private List<RequestItemDto> items;

    public RequestCreateDto() {
    }

    public RequestCreateDto(String studentName, List<RequestItemDto> items) {
        this.studentName = studentName;
        this.items = items;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public List<RequestItemDto> getItems() {
        return items;
    }

    public void setItems(List<RequestItemDto> items) {
        this.items = items;
    }
}
