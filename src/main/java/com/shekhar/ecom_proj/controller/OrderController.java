package com.shekhar.ecom_proj.controller;


import com.shekhar.ecom_proj.dto.OrderRequest;
import com.shekhar.ecom_proj.model.Order;
import com.shekhar.ecom_proj.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService){

        this.orderService = orderService;
    }

    // Create a new order
    @PostMapping
    public ResponseEntity<Order> createOrder(
            @RequestBody OrderRequest orderRequest,
            Authentication authentication
    ) {

        Order order = orderService.createOrder(orderRequest, authentication);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(order);
    }

    // Get logged-in user's orders
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {

        List<Order> orders = orderService.getMyOrders(authentication);

        return ResponseEntity.ok(orders);
    }

    // Get one order
    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(
            @PathVariable Long orderId
    ) {

        Order order = orderService.getOrderById(orderId);

        return ResponseEntity.ok(order);
    }

    // Cancel order
    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable Long orderId,
            Authentication authentication
    ) {

        Order order = orderService.cancelOrder(orderId, authentication);

        return ResponseEntity.ok(order);
    }
}
