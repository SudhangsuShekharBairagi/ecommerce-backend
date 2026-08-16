package com.shekhar.ecom_proj.controller;

import com.shekhar.ecom_proj.dto.UpdateStatusRequest;
import com.shekhar.ecom_proj.model.Order;
import com.shekhar.ecom_proj.model.OrderItem;
import com.shekhar.ecom_proj.service.AdminOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/admin/orders")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrdersController {
    private final AdminOrderService adminOrderService;

    public AdminOrdersController(AdminOrderService adminOrderService) {
        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public ResponseEntity<?> getAllOrders() {

      try {
          List<Order> orders = adminOrderService.getAllOrders();
          return ResponseEntity.ok(orders);
      } catch (Exception e) {
          return new ResponseEntity<>(
                  e.getMessage(),
                  HttpStatus.BAD_REQUEST
          );
      }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestBody UpdateStatusRequest request) {

        Order order = adminOrderService.updateStatus(id, request);

        return ResponseEntity.ok(order);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id) {
        List<OrderItem> orderItems = adminOrderService.getOrderItems(id);
        return ResponseEntity.ok(orderItems);
    }

}
