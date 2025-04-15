package com.project.simple_order.service.impl;

import com.project.simple_order.dto.ProductDto;
import com.project.simple_order.dto.ProductRequest;
import com.project.simple_order.entity.Category;
import com.project.simple_order.exceptions.InvalidCredentialsException;
import com.project.simple_order.mapper.EntityDtoMapper;
import com.project.simple_order.repositories.CategoryRepository;
import com.project.simple_order.repositories.ProductRepository;
import com.project.simple_order.service.ProductService;
import com.project.simple_order.utils.AppResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.project.simple_order.entity.Product;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final EntityDtoMapper entityDtoMapper;

    @Override
    public AppResponse createProduct(ProductRequest productRequest) throws ChangeSetPersister.NotFoundException {
        validateProductRequest(productRequest);
        Long categoryId = productRequest.getCategoryId();
        Category category = categoryRepository.findById(categoryId).orElseThrow(()-> new ChangeSetPersister.NotFoundException());

        var product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .stock(productRequest.getStock())
                .category(productRequest.getCategory())
                .imageUrl(productRequest.getImageUrl())
                .build();

        productRepository.save(product);
        return AppResponse.builder()
                .status(200)
                .message("Product successfully created")
                .build();
    }

    @Override
    public AppResponse getAllProducts() {
        List<ProductDto> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(entityDtoMapper::mapProductToDtoBasic)
                .collect(Collectors.toList());

        return AppResponse.builder()
                .status(200)
                .productList(productList)
                .build();

    }

    @Override
    public AppResponse deleteProduct(Long productId) throws ChangeSetPersister.NotFoundException {
        Product product = productRepository.findById(productId).orElseThrow(()-> new ChangeSetPersister.NotFoundException());
        productRepository.delete(product);

        return AppResponse.builder()
                .status(200)
                .message("Product deleted successfully")
                .build();
    }

    private void validateProductRequest(ProductRequest request) {
        if (request.getCategory() == null ||
                request.getImageUrl() == null || request.getImageUrl().isBlank() ||
                request.getName() == null || request.getName().isBlank() ||
                request.getDescription() == null || request.getDescription().isBlank() ||
                request.getPrice() == null) {
            throw new InvalidCredentialsException("All fields are required.");
        }
    }

}
