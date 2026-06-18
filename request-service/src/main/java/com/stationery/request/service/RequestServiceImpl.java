package com.stationery.request.service;

import com.stationery.request.client.InventoryClient;
import com.stationery.request.dto.RequestCreateDto;
import com.stationery.request.dto.RequestItemDto;
import com.stationery.request.dto.RequestResponseDto;
import com.stationery.request.dto.client.InventoryResponse;
import com.stationery.request.dto.client.StockUpdateRequest;
import com.stationery.request.entity.Request;
import com.stationery.request.entity.RequestItem;
import com.stationery.request.enums.RequestStatus;
import com.stationery.request.exception.InsufficientStockException;
import com.stationery.request.exception.ResourceNotFoundException;
import com.stationery.request.repository.RequestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation handling business logic for stationery requests.
 */
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final InventoryClient inventoryClient;

    public RequestServiceImpl(RequestRepository requestRepository, InventoryClient inventoryClient) {
        this.requestRepository = requestRepository;
        this.inventoryClient = inventoryClient;
    }

    @Override
    @Transactional
    public RequestResponseDto createRequest(RequestCreateDto requestDto, String studentEmail) {
        Request request = Request.builder()
                .studentEmail(studentEmail)
                .studentName(requestDto.getStudentName())
                .status(RequestStatus.PENDING)
                .build();

        for (RequestItemDto itemDto : requestDto.getItems()) {
            RequestItem item = RequestItem.builder()
                    .inventoryItemId(itemDto.getInventoryItemId())
                    .itemName(itemDto.getItemName())
                    .quantity(itemDto.getQuantity())
                    .build();
            request.addRequestItem(item);
        }

        Request saved = requestRepository.save(request);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestResponseDto> getStudentRequests(String studentEmail) {
        return requestRepository.findByStudentEmail(studentEmail).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public RequestResponseDto getRequestById(Long id, String email, boolean isAdmin) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));

        if (!isAdmin && !request.getStudentEmail().equals(email)) {
            throw new IllegalArgumentException("You are not authorized to view this request.");
        }

        return mapToResponse(request);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RequestResponseDto> getAllRequests() {
        return requestRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public RequestResponseDto approveRequest(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be approved. Current status: " + request.getStatus());
        }

        // Pass 1: Verify all items have enough stock
        for (RequestItem item : request.getItems()) {
            InventoryResponse inventoryItem;
            try {
                inventoryItem = inventoryClient.getItemById(item.getInventoryItemId());
            } catch (Exception e) {
                throw new ResourceNotFoundException("Inventory item not found for ID: " + item.getInventoryItemId() + " (" + item.getItemName() + ")");
            }

            if (inventoryItem.getQuantity() < item.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for item: " + item.getItemName() 
                        + ". Available: " + inventoryItem.getQuantity() + ", Requested: " + item.getQuantity());
            }
        }

        // Pass 2: Deduct stock from Inventory Service
        for (RequestItem item : request.getItems()) {
            StockUpdateRequest stockRequest = new StockUpdateRequest(item.getQuantity(), "REDUCE");
            inventoryClient.updateStock(item.getInventoryItemId(), stockRequest);
        }

        request.setStatus(RequestStatus.APPROVED);
        Request updated = requestRepository.save(request);
        return mapToResponse(updated);
    }

    @Override
    @Transactional
    public RequestResponseDto rejectRequest(Long id) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Request not found with ID: " + id));

        if (request.getStatus() != RequestStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be rejected. Current status: " + request.getStatus());
        }

        request.setStatus(RequestStatus.REJECTED);
        Request updated = requestRepository.save(request);
        return mapToResponse(updated);
    }

    private RequestResponseDto mapToResponse(Request request) {
        List<RequestItemDto> itemDtos = request.getItems().stream()
                .map(item -> new RequestItemDto(item.getInventoryItemId(), item.getItemName(), item.getQuantity()))
                .collect(Collectors.toList());

        return new RequestResponseDto(
                request.getId(),
                request.getStudentEmail(),
                request.getStudentName(),
                request.getStatus().name(),
                itemDtos,
                request.getCreatedAt(),
                request.getUpdatedAt()
        );
    }
}
