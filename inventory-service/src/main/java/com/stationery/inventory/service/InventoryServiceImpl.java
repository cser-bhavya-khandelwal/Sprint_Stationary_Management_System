package com.stationery.inventory.service;

import com.stationery.inventory.dto.InventoryRequest;
import com.stationery.inventory.dto.InventoryResponse;
import com.stationery.inventory.dto.StockUpdateRequest;
import com.stationery.inventory.entity.InventoryItem;
import com.stationery.inventory.exception.InsufficientStockException;
import com.stationery.inventory.exception.ResourceNotFoundException;
import com.stationery.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for managing inventory logic.
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryServiceImpl(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    @Transactional
    public InventoryResponse createItem(InventoryRequest request) {
        if (inventoryRepository.existsByItemCode(request.getItemCode())) {
            throw new IllegalArgumentException("Item code already exists: " + request.getItemCode());
        }

        InventoryItem item = InventoryItem.builder()
                .itemCode(request.getItemCode())
                .name(request.getName())
                .description(request.getDescription())
                .quantity(request.getQuantity())
                .minQuantityThreshold(request.getMinQuantityThreshold())
                .price(request.getPrice())
                .category(request.getCategory())
                .build();

        InventoryItem savedItem = inventoryRepository.save(item);
        return mapToResponse(savedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getAllItems() {
        return inventoryRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InventoryResponse getItemById(Long id) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + id));
        return mapToResponse(item);
    }

    @Override
    @Transactional
    public InventoryResponse updateItem(Long id, InventoryRequest request) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + id));

        if (!item.getItemCode().equals(request.getItemCode()) && 
                inventoryRepository.existsByItemCode(request.getItemCode())) {
            throw new IllegalArgumentException("Item code already exists: " + request.getItemCode());
        }

        item.setItemCode(request.getItemCode());
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setQuantity(request.getQuantity());
        item.setMinQuantityThreshold(request.getMinQuantityThreshold());
        item.setPrice(request.getPrice());
        item.setCategory(request.getCategory());

        InventoryItem updatedItem = inventoryRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Override
    @Transactional
    public void deleteItem(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Inventory item not found with ID: " + id);
        }
        inventoryRepository.deleteById(id);
    }

    @Override
    @Transactional
    public InventoryResponse updateStock(Long id, StockUpdateRequest request) {
        InventoryItem item = inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory item not found with ID: " + id));

        int currentStock = item.getQuantity();
        int amount = request.getAmount();
        String action = request.getAction().toUpperCase();

        switch (action) {
            case "ADD":
                item.setQuantity(currentStock + amount);
                break;
            case "REDUCE":
                if (currentStock < amount) {
                    throw new InsufficientStockException("Insufficient stock. Available: " + currentStock 
                            + ", Requested reduction: " + amount);
                }
                item.setQuantity(currentStock - amount);
                break;
            case "SET":
                if (amount < 0) {
                    throw new IllegalArgumentException("Quantity cannot be set to a negative value");
                }
                item.setQuantity(amount);
                break;
            default:
                throw new IllegalArgumentException("Invalid action type: " + action + ". Allowed: ADD, REDUCE, SET");
        }

        InventoryItem updatedItem = inventoryRepository.save(item);
        return mapToResponse(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> searchInventory(String query) {
        return inventoryRepository.searchInventory(query).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<InventoryResponse> getLowStockItems() {
        return inventoryRepository.findLowStockItems().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private InventoryResponse mapToResponse(InventoryItem item) {
        boolean isLowStock = item.getQuantity() <= item.getMinQuantityThreshold();
        return new InventoryResponse(
                item.getId(),
                item.getItemCode(),
                item.getName(),
                item.getDescription(),
                item.getQuantity(),
                item.getMinQuantityThreshold(),
                item.getPrice(),
                item.getCategory(),
                isLowStock,
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}
