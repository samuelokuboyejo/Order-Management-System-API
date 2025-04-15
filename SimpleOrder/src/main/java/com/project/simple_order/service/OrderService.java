package com.project.simple_order.service;

import com.project.simple_order.dto.OrderRequest;
import com.project.simple_order.utils.AppResponse;
import org.springframework.data.crossstore.ChangeSetPersister;

public interface OrderService {
    AppResponse placeOrder(OrderRequest orderRequest);

    AppResponse updateOrderItemStatus(Long orderItemId, String status) throws ChangeSetPersister.NotFoundException;

    AppResponse getAllOrdersForUser();
}
