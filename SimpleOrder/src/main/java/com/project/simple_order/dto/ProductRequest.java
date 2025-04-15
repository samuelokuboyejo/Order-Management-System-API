package com.project.simple_order.dto;

import com.project.simple_order.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String name;

    private  String description;

    private BigDecimal price;

    private String imageUrl;

    private int stock;

    private Category category;

    private Long categoryId;
}
