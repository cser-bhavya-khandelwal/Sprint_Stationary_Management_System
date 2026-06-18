package com.stationery.inventory.service;

import com.stationery.inventory.dto.InventoryRequest;
import com.stationery.inventory.dto.InventoryResponse;
import com.stationery.inventory.dto.StockUpdateRequest;
import com.stationery.inventory.entity.InventoryItem;
import com.stationery.inventory.exception.InsufficientStockException;
import com.stationery.inventory.exception.ResourceNotFoundException;
import com.stationery.inventory.repository.InventoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItem_Success() {
        InventoryRequest request = new InventoryRequest("ITEM-001", "Blue Pen", "Blue ballpoint pen", 100, 10, 1.5, "Writing");
        when(inventoryRepository.existsByItemCode(request.getItemCode())).thenReturn(false);

        InventoryItem savedItem = InventoryItem.builder()
                .id(1L)
                .itemCode(request.getItemCode())
                .name(request.getName())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .minQuantityThreshold(request.getMinQuantityThreshold())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

        when(inventoryRepository.save(any(InventoryItem.class))).thenReturn(savedItem);

        InventoryResponse response = inventoryService.createItem(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Blue Pen", response.getName());
        verify(inventoryRepository, times(1)).save(any(InventoryItem.class));
    }

    @Test
    void createItem_ThrowsException_WhenCodeExists() {
        InventoryRequest request = new InventoryRequest("ITEM-001", "Blue Pen", "Blue ballpoint pen", 100, 10, 1.5, "Writing");
        when(inventoryRepository.existsByItemCode(request.getItemCode())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> inventoryService.createItem(request));
        verify(inventoryRepository, never()).save(any(InventoryItem.class));
    }

    @Test
    void getItemById_Success() {
        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .itemCode("ITEM-001")
                .name("Blue Pen")
                .quantity(100)
                .minQuantityThreshold(10)
                .build();

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item));

        InventoryResponse response = inventoryService.getItemById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Blue Pen", response.getName());
    }

    @Test
    void getItemById_ThrowsException_WhenNotFound() {
        when(inventoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> inventoryService.getItemById(1L));
    }

    @Test
    void updateStock_Add_Success() {
        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .quantity(100)
                .minQuantityThreshold(10)
                .build();

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StockUpdateRequest request = new StockUpdateRequest(50, "ADD");
        InventoryResponse response = inventoryService.updateStock(1L, request);

        assertEquals(150, response.getQuantity());
    }

    @Test
    void updateStock_Reduce_Success() {
        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .quantity(100)
                .minQuantityThreshold(10)
                .build();

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item));
        when(inventoryRepository.save(any(InventoryItem.class))).thenAnswer(invocation -> invocation.getArgument(0));

        StockUpdateRequest request = new StockUpdateRequest(40, "REDUCE");
        InventoryResponse response = inventoryService.updateStock(1L, request);

        assertEquals(60, response.getQuantity());
    }

    @Test
    void updateStock_Reduce_ThrowsException_WhenInsufficient() {
        InventoryItem item = InventoryItem.builder()
                .id(1L)
                .quantity(10)
                .minQuantityThreshold(2)
                .build();

        when(inventoryRepository.findById(1L)).thenReturn(Optional.of(item));

        StockUpdateRequest request = new StockUpdateRequest(20, "REDUCE");
        assertThrows(InsufficientStockException.class, () -> inventoryService.updateStock(1L, request));
    }
}
