package com.stationery.request.entity;

import com.stationery.request.enums.RequestStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a student request for stationery.
 */
@Entity
@Table(name = "requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_email", nullable = false)
    private String studentEmail;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<RequestItem> items = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Request() {
    }

    public Request(Long id, String studentEmail, String studentName, RequestStatus status, 
                   List<RequestItem> items, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.studentEmail = studentEmail;
        this.studentName = studentName;
        this.status = status;
        this.items = items != null ? items : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Helper to add request items
    public void addRequestItem(RequestItem item) {
        items.add(item);
        item.setRequest(this);
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

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public List<RequestItem> getItems() {
        return items;
    }

    public void setItems(List<RequestItem> items) {
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

    public static RequestBuilder builder() {
        return new RequestBuilder();
    }

    public static class RequestBuilder {
        private Long id;
        private String studentEmail;
        private String studentName;
        private RequestStatus status;
        private List<RequestItem> items;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        RequestBuilder() {
        }

        public RequestBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RequestBuilder studentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
            return this;
        }

        public RequestBuilder studentName(String studentName) {
            this.studentName = studentName;
            return this;
        }

        public RequestBuilder status(RequestStatus status) {
            this.status = status;
            return this;
        }

        public RequestBuilder items(List<RequestItem> items) {
            this.items = items;
            return this;
        }

        public RequestBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RequestBuilder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Request build() {
            return new Request(id, studentEmail, studentName, status, items, createdAt, updatedAt);
        }
    }
}
