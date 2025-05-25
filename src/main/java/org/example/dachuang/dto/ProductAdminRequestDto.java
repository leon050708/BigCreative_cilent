package org.example.dachuang.dto; // 替换为您的实际包名

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductAdminRequestDto {
    @NotBlank(message = "产品名称不能为空")
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull(message = "价格不能为空")
    @Min(0)
    private Double price;

    private String imageUrl;

    @NotNull(message = "库存不能为空")
    @Min(0)
    private Integer stock;

    private Boolean isRecommended = false;

    @NotNull(message = "分类ID不能为空")
    private Long categoryId; // 直接接收分类ID
}