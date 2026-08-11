package com.shekhar.ecom_proj.repo;

import com.shekhar.ecom_proj.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}