package com.project.simple_order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long id;

    private String name;

    private  String description;

    private BigDecimal price;

    private String imageUrl;

    private CategoryDto category;
}
