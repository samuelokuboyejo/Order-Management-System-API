package com.project.simple_order.dto;

import lombok.Data;

@Data
public class OrderItemRequest {
    private Long productId;

    private int quantity;
}
