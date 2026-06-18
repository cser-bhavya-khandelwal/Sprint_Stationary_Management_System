package com.stationery.request.dto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO representing requests response detail payload.
 */
public class RequestResponseDto {

    private Long id;
    private String studentEmail;
    private String studentName;
    private String status;
    private List<RequestItemDto> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RequestResponseDto() {
    }

    public RequestResponseDto(Long id, String studentEmail, String studentName, String status, 
                              List<RequestItemDto> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.status = status;
        this.items = items;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<RequestItemDto> getItems() {
        return items;
    }

    public void setItems(List<RequestItemDto> items) {
        this.items = items;
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
