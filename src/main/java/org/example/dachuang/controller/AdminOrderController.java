package org.example.dachuang.controller;

import org.example.dachuang.dto.OrderResponseDto;
import org.example.dachuang.dto.OrderStatusUpdateRequestDto;
import org.example.dachuang.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
// @PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrdersForAdmin() {
        // 复用 OrderService 中的 getAllOrders 方法
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> getOrderByIdForAdmin(@PathVariable Long id) {
        // 现在使用 OrderService 中的 getOrderById 方法
        OrderResponseDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
        // throw new UnsupportedOperationException("getOrderByIdForAdmin not fully implemented yet."); // 旧代码
    }


    @PatchMapping("/{orderId}/status") // 使用 PATCH 部分更新，或 PUT
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusUpdateRequestDto statusUpdateRequestDto) {
        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, statusUpdateRequestDto.getStatus());
        return ResponseEntity.ok(updatedOrder);
    }
}