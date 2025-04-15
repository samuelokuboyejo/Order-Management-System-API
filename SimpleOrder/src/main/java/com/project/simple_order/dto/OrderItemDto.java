package com.project.simple_order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDto {

    private Long id;

    private int quantity;

    private BigDecimal price;

    private String  status;

    private  UserDto user;

    private ProductDto product;

    private LocalDateTime createdAt;
}
