package org.example.dachuang.repository;

import org.example.dachuang.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
// 如果需要按用户查找，可以添加 findByUserId(Long userId);
}
