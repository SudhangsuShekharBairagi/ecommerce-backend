package com.shekhar.ecom_proj.service;

import com.shekhar.ecom_proj.dto.UpdateStatusRequest;
import com.shekhar.ecom_proj.model.Order;
import com.shekhar.ecom_proj.model.OrderItem;
import com.shekhar.ecom_proj.repo.OrderItemRepository;
import com.shekhar.ecom_proj.repo.OrderRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminOrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminOrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    @Transactional
    public Order updateStatus(Long id, UpdateStatusRequest request) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found with id: " + id
                        ));

        if (request.getStatus() == null) {
            throw new RuntimeException(
                    "Order status cannot be null"
            );
        }

        order.setStatus(request.getStatus());

        return orderRepository.save(order);
    }
    public List<OrderItem> getOrderItems(Long id) {
        List<OrderItem> orderItems =
                orderItemRepository.findByOrderId(id);

        if (orderItems.isEmpty()) {
            throw new RuntimeException("Order items not found");
        }

        return orderItems;
    }
}
