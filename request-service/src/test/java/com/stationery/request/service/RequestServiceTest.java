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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RequestServiceTest {

    @Mock
    private RequestRepository requestRepository;

    @Mock
    private InventoryClient inventoryClient;

    @InjectMocks
    private RequestServiceImpl requestService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createRequest_Success() {
        RequestItemDto itemDto = new RequestItemDto(1L, "Blue Pen", 5);
        RequestCreateDto createDto = new RequestCreateDto("Test Student", Collections.singletonList(itemDto));

        Request savedRequest = Request.builder()
                .id(1L)
                .studentEmail("student@test.com")
                .studentName("Test Student")
                .status(RequestStatus.PENDING)
                .build();
        RequestItem item = RequestItem.builder()
                .id(1L)
                .inventoryItemId(1L)
                .itemName("Blue Pen")
                .quantity(5)
                .build();
        savedRequest.addRequestItem(item);

        when(requestRepository.save(any(Request.class))).thenReturn(savedRequest);

        RequestResponseDto response = requestService.createRequest(createDto, "student@test.com");

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("PENDING", response.getStatus());
        assertEquals(1, response.getItems().size());
        verify(requestRepository, times(1)).save(any(Request.class));
    }

    @Test
    void approveRequest_Success() {
        Request request = Request.builder()
                .id(1L)
                .studentEmail("student@test.com")
                .studentName("Test Student")
                .status(RequestStatus.PENDING)
                .build();
        RequestItem item = RequestItem.builder()
                .id(1L)
                .inventoryItemId(1L)
                .itemName("Blue Pen")
                .quantity(5)
                .build();
        request.addRequestItem(item);

        InventoryResponse inventoryItem = new InventoryResponse();
        inventoryItem.setId(1L);
        inventoryItem.setName("Blue Pen");
        inventoryItem.setQuantity(100);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(inventoryClient.getItemById(1L)).thenReturn(inventoryItem);
        when(requestRepository.save(any(Request.class))).thenAnswer(inv -> inv.getArgument(0));

        RequestResponseDto response = requestService.approveRequest(1L);

        assertEquals("APPROVED", response.getStatus());
        verify(inventoryClient, times(1)).updateStock(eq(1L), any(StockUpdateRequest.class));
    }

    @Test
    void approveRequest_ThrowsException_WhenStockInsufficient() {
        Request request = Request.builder()
                .id(1L)
                .studentEmail("student@test.com")
                .status(RequestStatus.PENDING)
                .build();
        RequestItem item = RequestItem.builder()
                .id(1L)
                .inventoryItemId(1L)
                .itemName("Blue Pen")
                .quantity(10)
                .build();
        request.addRequestItem(item);

        InventoryResponse inventoryItem = new InventoryResponse();
        inventoryItem.setId(1L);
        inventoryItem.setName("Blue Pen");
        inventoryItem.setQuantity(5);

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(inventoryClient.getItemById(1L)).thenReturn(inventoryItem);

        assertThrows(InsufficientStockException.class, () -> requestService.approveRequest(1L));
        verify(inventoryClient, never()).updateStock(anyLong(), any());
    }

    @Test
    void rejectRequest_Success() {
        Request request = Request.builder()
                .id(1L)
                .studentEmail("student@test.com")
                .status(RequestStatus.PENDING)
                .build();

        when(requestRepository.findById(1L)).thenReturn(Optional.of(request));
        when(requestRepository.save(any(Request.class))).thenAnswer(inv -> inv.getArgument(0));

        RequestResponseDto response = requestService.rejectRequest(1L);

        assertEquals("REJECTED", response.getStatus());
    }
}
