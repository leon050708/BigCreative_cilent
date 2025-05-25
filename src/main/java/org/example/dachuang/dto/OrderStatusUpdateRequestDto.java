package org.example.dachuang.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusUpdateRequestDto {
    @NotBlank(message = "订单状态不能为空")
    private String status; // 例如: "Shipped", "Delivered", "Cancelled"
}
