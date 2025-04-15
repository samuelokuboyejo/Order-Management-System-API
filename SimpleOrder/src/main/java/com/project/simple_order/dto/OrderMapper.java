package com.project.simple_order.dto;

import com.project.simple_order.entity.Order;
import com.project.simple_order.entity.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Component
public class OrderMapper {

        public OrderDto toDto(Order order) {
            List<OrderItemDto> itemDtos = order.getOrderItemList().stream()
                    .map(this::toOrderItemDto)
                    .collect(Collectors.toList());

            return OrderDto.builder()
                    .id(order.getId())
                    .totalPrice(order.getTotalPrice())
                    .createdAt(order.getCreatedAt())
                    .build();
        }

        private OrderItemDto toOrderItemDto(OrderItem item) {
            return OrderItemDto.builder()
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .build();
        }
    }

