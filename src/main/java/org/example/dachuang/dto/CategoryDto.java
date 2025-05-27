package org.example.dachuang.dto;

import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    private String name;
    private String imageUrl;
    private String mainCategory; // 新增：大类字段
}