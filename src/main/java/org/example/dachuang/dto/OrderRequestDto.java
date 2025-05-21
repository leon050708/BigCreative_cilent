package org.example.dachuang.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import java.util.List;

@Data
public class OrderRequestDto {
    @NotEmpty
    @Valid // 验证列表中的每个元素
    private List<OrderItemRequestDto> items;
    // totalAmount 可以在后端计算，以确保准确性
}