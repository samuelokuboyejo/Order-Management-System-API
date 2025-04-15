package com.project.simple_order.controllers;

import com.project.simple_order.dto.ProductRequest;
import com.project.simple_order.service.ProductService;
import com.project.simple_order.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<AppResponse> createProduct(ProductRequest productRequest) throws ChangeSetPersister.NotFoundException {
        AppResponse response = productService.createProduct(productRequest);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/get-all")
    public ResponseEntity<AppResponse> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<AppResponse> deleteProduct(@PathVariable Long productId) throws ChangeSetPersister.NotFoundException {
        return ResponseEntity.ok(productService.deleteProduct(productId));

    }
}
