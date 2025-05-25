package org.example.dachuang.service;


import org.example.dachuang.dto.OrderItemRequestDto;
import org.example.dachuang.dto.OrderRequestDto;
import org.example.dachuang.dto.OrderResponseDto;
import org.example.dachuang.dto.OrderItemResponseDto;
import org.example.dachuang.exception.ResourceNotFoundException;
import org.example.dachuang.model.Order;
import org.example.dachuang.model.OrderItem;
import org.example.dachuang.model.Product;
import org.example.dachuang.repository.OrderRepository;
import org.example.dachuang.repository.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.dachuang.model.Order;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    // --- DTO转换辅助方法 ---
    private OrderResponseDto convertToOrderResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        BeanUtils.copyProperties(order, dto, "items"); // 排除items，手动转换
        dto.setItems(order.getItems().stream().map(this::convertToOrderItemResponseDto).collect(Collectors.toList()));
        return dto;
    }

    private OrderItemResponseDto convertToOrderItemResponseDto(OrderItem orderItem) {
        OrderItemResponseDto dto = new OrderItemResponseDto();
        BeanUtils.copyProperties(orderItem, dto);
        return dto;
    }
    // --- End DTO转换 ---

    @Transactional
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        Order order = new Order();
        order.setStatus("Processing"); // 初始状态
        order.setItems(new ArrayList<>());

        double totalAmount = 0.0;

        for (OrderItemRequestDto itemDto : orderRequestDto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemDto.getProductId()));

            if (product.getStock() < itemDto.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setPriceAtOrder(product.getPrice()); // 记录下单时价格
            orderItem.setImageUrl(product.getImageUrl());
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setOrder(order); // 关联到当前订单
            order.getItems().add(orderItem);
            totalAmount += product.getPrice() * itemDto.getQuantity();

            // 更新库存
            product.setStock(product.getStock() - itemDto.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(savedOrder);
    }

    @Transactional(readOnly = true)
    public List<OrderResponseDto> getAllOrders() {
        // 在真实应用中，这里应该根据当前登录用户获取订单
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // TODO: 在这里可以添加校验逻辑，例如检查状态转换是否有效
        // (e.g., 不能从 "Delivered" 改回 "Processing")
        // TODO: 如果状态变为 "Cancelled"，可能需要恢复商品库存

        order.setStatus(newStatus);
        Order updatedOrder = orderRepository.save(order);
        return convertToOrderResponseDto(updatedOrder);
    }
}
