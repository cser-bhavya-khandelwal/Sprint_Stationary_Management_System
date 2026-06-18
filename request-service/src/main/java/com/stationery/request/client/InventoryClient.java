package com.stationery.request.client;

import com.stationery.request.dto.client.InventoryResponse;
import com.stationery.request.dto.client.StockUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Feign Client for calling endpoints of the Inventory Service.
 */
@FeignClient(name = "inventory-service")
public interface InventoryClient {

    @GetMapping("/api/inventory/{id}")
    InventoryResponse getItemById(@PathVariable("id") Long id);

    @PutMapping("/api/inventory/{id}/stock")
    InventoryResponse updateStock(@PathVariable("id") Long id, @RequestBody StockUpdateRequest request);
}
