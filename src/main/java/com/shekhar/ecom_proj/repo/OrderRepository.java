package com.shekhar.ecom_proj.repo;

import com.shekhar.ecom_proj.model.Order;
import com.shekhar.ecom_proj.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Users user);

    Optional<Order> findByOrderNumber(String orderNumber);
}