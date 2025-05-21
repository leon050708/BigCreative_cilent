package org.example.dachuang.dto;

import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Long productId;
    private String productName;
    private Double priceAtOrder;
    private String imageUrl;
    private Integer quantity;
}
