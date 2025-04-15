package com.project.simple_order.utils;

import com.project.simple_order.dto.ProductDto;
import com.project.simple_order.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppResponse {
    private int status;

    private String message;

    @CreatedDate
    private LocalDateTime timestamp;

    private UserDto user;

    private ProductDto product;
    private List<ProductDto> productList;

    private Object data;


}
