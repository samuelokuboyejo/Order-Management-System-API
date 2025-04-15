package com.project.simple_order.controllers;

import com.project.simple_order.dto.OrderRequest;
import com.project.simple_order.service.OrderService;
import com.project.simple_order.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderItemController {

    private final OrderService orderService;

    @PostMapping("/create")
    public ResponseEntity<AppResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        AppResponse response = orderService.placeOrder(orderRequest);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/items/{orderItemId}/status")
    public ResponseEntity<AppResponse> updateOrderItemStatus(
            @PathVariable Long orderItemId,
            @RequestParam String status) throws ChangeSetPersister.NotFoundException {

        AppResponse response = orderService.updateOrderItemStatus(orderItemId, status);
        return ResponseEntity.ok(response);
    }


}
