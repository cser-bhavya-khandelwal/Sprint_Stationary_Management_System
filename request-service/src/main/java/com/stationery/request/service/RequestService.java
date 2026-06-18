package com.stationery.request.service;

import com.stationery.request.dto.RequestCreateDto;
import com.stationery.request.dto.RequestResponseDto;

import java.util.List;

/**
 * Service interface defining stationery requests actions.
 */
public interface RequestService {

    /**
     * Submits a new request for the currently logged-in student.
     */
    RequestResponseDto createRequest(RequestCreateDto request, String studentEmail);

    /**
     * Retrieves all requests submitted by the logged-in student.
     */
    List<RequestResponseDto> getStudentRequests(String studentEmail);

    /**
     * Retrieves details of a specific request by its ID.
     */
    RequestResponseDto getRequestById(Long id, String email, boolean isAdmin);

    /**
     * Retrieves all stationery requests (Admin only).
     */
    List<RequestResponseDto> getAllRequests();

    /**
     * Approves a stationery request, checking inventory stock and adjusting quantity (Admin only).
     */
    RequestResponseDto approveRequest(Long id);

    /**
     * Rejects a stationery request (Admin only).
     */
    RequestResponseDto rejectRequest(Long id);
}
