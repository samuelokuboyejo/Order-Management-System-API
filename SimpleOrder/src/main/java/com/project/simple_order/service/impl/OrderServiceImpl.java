package com.project.simple_order.service.impl;

import com.project.simple_order.auth.entities.User;
import com.project.simple_order.dto.OrderDto;
import com.project.simple_order.dto.OrderItemRequest;
import com.project.simple_order.dto.OrderMapper;
import com.project.simple_order.dto.OrderRequest;
import com.project.simple_order.entity.Order;
import com.project.simple_order.entity.OrderItem;
import com.project.simple_order.entity.Product;
import com.project.simple_order.enums.OrderStatus;
import com.project.simple_order.exceptions.ProductOutOfStockException;
import com.project.simple_order.mapper.EntityDtoMapper;
import com.project.simple_order.repositories.OrderItemRepository;
import com.project.simple_order.repositories.OrderRepository;
import com.project.simple_order.repositories.ProductRepository;
import com.project.simple_order.service.OrderService;
import com.project.simple_order.utils.AppResponse;
import com.project.simple_order.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final EntityDtoMapper entityDtoMapper;
    private final OrderMapper orderMapper;


    @Override
    public AppResponse placeOrder(OrderRequest orderRequest) {
        User user = userService.getAuthenticatedUser();

//      Validating all products before proceeding
        validateProductStock(orderRequest.getItems());

//       Building OrderItems and updating stock
        List<OrderItem> orderItems = orderRequest.getItems().stream().map(itemRequest -> {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + itemRequest.getProductId()));

            int quantity = itemRequest.getQuantity();
            product.setStock(product.getStock() - quantity);
//          Saving updated stock
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(quantity);
            orderItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            orderItem.setStatus(OrderStatus.PENDING);
            orderItem.setUser(user);

            return orderItem;
        }).collect(Collectors.toList());

//            Calculating total price
        BigDecimal totalPrice = orderRequest.getTotalPrice() != null && orderRequest.getTotalPrice().compareTo(BigDecimal.ZERO) > 0
                ? orderRequest.getTotalPrice()
                : orderItems.stream().map(OrderItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);

//           Saving the order
        Order order = new Order();
        order.setOrderItemList(orderItems);
        order.setTotalPrice(totalPrice);
        orderItems.forEach(item -> item.setOrder(order));

        orderRepository.save(order);

        return AppResponse.builder()
                .status(HttpStatus.OK.value())
                .message("Order placed successfully.")
                .build();
    }

    private void validateProductStock(List<OrderItemRequest> itemRequests) {
        for (OrderItemRequest itemRequest : itemRequests) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + itemRequest.getProductId()));

            if (product.getStock() < itemRequest.getQuantity()) {
                throw new ProductOutOfStockException("Insufficient stock for product: " + product.getName());
            }
        }
    }

    @Override
    public AppResponse updateOrderItemStatus(Long orderItemId, String status) throws ChangeSetPersister.NotFoundException {
        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(()-> new ChangeSetPersister.NotFoundException());

        orderItem.setStatus(OrderStatus.valueOf(status.toUpperCase()));
        orderItemRepository.save(orderItem);
        return AppResponse.builder()
                .status(200)
                .message("Order status updated successfully")
                .build();
    }

    @Override
    public AppResponse getAllOrdersForUser() {
        User user = userService.getAuthenticatedUser();
        List<Order> orders = orderRepository.findAllByUser(user);

        List<OrderDto> orderDtos = new ArrayList<>();
        orders.forEach(order -> orderDtos.add(orderMapper.toDto(order)));

        return AppResponse.builder()
                .status(200)
                .message("Fetched all orders for user")
                .data(orderDtos)
                .build();
    }
}
