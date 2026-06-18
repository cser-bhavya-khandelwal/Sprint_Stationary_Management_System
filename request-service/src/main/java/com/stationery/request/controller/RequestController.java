package com.stationery.request.controller;

import com.stationery.request.dto.RequestCreateDto;
import com.stationery.request.dto.RequestResponseDto;
import com.stationery.request.service.RequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

/**
 * Controller exposing REST endpoints for submitting and processing stationery requests.
 */
@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    /**
     * POST /api/requests : Submits a new stationery request.
     */
    @PostMapping
    public ResponseEntity<RequestResponseDto> createRequest(
            @Valid @RequestBody RequestCreateDto requestDto,
            Principal principal
    ) {
        RequestResponseDto response = requestService.createRequest(requestDto, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * GET /api/requests/my : Retrieves logged-in student's own requests.
     */
    @GetMapping("/my")
    public ResponseEntity<List<RequestResponseDto>> getMyRequests(Principal principal) {
        List<RequestResponseDto> response = requestService.getStudentRequests(principal.getName());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/requests/{id} : Retrieves details of a specific request.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RequestResponseDto> getRequestById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        boolean isAdmin = authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        RequestResponseDto response = requestService.getRequestById(id, authentication.getName(), isAdmin);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/requests : Retrieves all stationery requests (Admin only).
     */
    @GetMapping
    public ResponseEntity<List<RequestResponseDto>> getAllRequests() {
        List<RequestResponseDto> response = requestService.getAllRequests();
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/requests/{id}/approve : Approves a request (Admin only).
     */
    @PutMapping("/{id}/approve")
    public ResponseEntity<RequestResponseDto> approveRequest(@PathVariable Long id) {
        RequestResponseDto response = requestService.approveRequest(id);
        return ResponseEntity.ok(response);
    }

    /**
     * PUT /api/requests/{id}/reject : Rejects a request (Admin only).
     */
    @PutMapping("/{id}/reject")
    public ResponseEntity<RequestResponseDto> rejectRequest(@PathVariable Long id) {
        RequestResponseDto response = requestService.rejectRequest(id);
        return ResponseEntity.ok(response);
    }
}
