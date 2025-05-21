package org.example.dachuang.dto;


import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Integer stock;
    private Boolean isRecommended;
    private CategoryDto category; //嵌套DTO
}