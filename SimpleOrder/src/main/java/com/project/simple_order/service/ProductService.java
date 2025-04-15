package com.project.simple_order.service;

import com.project.simple_order.dto.ProductRequest;
import com.project.simple_order.utils.AppResponse;
import org.springframework.data.crossstore.ChangeSetPersister;

import java.math.BigDecimal;

public interface ProductService {

    AppResponse createProduct(ProductRequest productRequest) throws ChangeSetPersister.NotFoundException;
    AppResponse deleteProduct(Long productId) throws ChangeSetPersister.NotFoundException;
    AppResponse getAllProducts();
}
