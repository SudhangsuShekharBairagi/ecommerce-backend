package com.shekhar.ecom_proj.controller;


import com.shekhar.ecom_proj.dto.OrderRequest;
import com.shekhar.ecom_proj.dto.UserOrderDto;
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
    public ResponseEntity<String> createOrder(
            @RequestBody OrderRequest orderRequest,
            Authentication authentication
    ) {

       try {
           String orderNumber = orderService.createOrder(orderRequest, authentication);

           return ResponseEntity
                   .status(HttpStatus.CREATED)
                   .body(orderNumber);
       } catch (Exception e) {

           return ResponseEntity
                   .status(HttpStatus.INTERNAL_SERVER_ERROR)
                   .body("Failed to create order");
       }
    }

    // Get logged-in user's orders
    @GetMapping
    public ResponseEntity<List<UserOrderDto>> getMyOrders(Authentication authentication) {

        List<UserOrderDto> orders = orderService.getMyOrders(authentication);

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
    @PatchMapping("/{orderNumber}/cancel")
    public ResponseEntity<Order> cancelOrder(
            @PathVariable String orderNumber,
            Authentication authentication
    ) {

        Order order = orderService.cancelOrder(orderNumber, authentication);

        return ResponseEntity.ok(order);
    }
}
