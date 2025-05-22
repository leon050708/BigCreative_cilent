package org.example.dachuang.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String imageUrl; // 新增字段
}