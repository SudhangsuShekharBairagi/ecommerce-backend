package com.shekhar.ecom_proj.service;

import com.shekhar.ecom_proj.dto.OrderItemRequest;
import com.shekhar.ecom_proj.dto.OrderRequest;
import com.shekhar.ecom_proj.enums.OrderStatus;
import com.shekhar.ecom_proj.model.Order;
import com.shekhar.ecom_proj.model.OrderItem;
import com.shekhar.ecom_proj.model.Product;
import com.shekhar.ecom_proj.model.Users;
import com.shekhar.ecom_proj.repo.OrderRepository;
import com.shekhar.ecom_proj.repo.ProductRepo;
import com.shekhar.ecom_proj.repo.UsersRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UsersRepository usersRepository;
    private final ProductRepo productRepo;

    public OrderService(OrderRepository orderRepository, UsersRepository usersRepository, ProductRepo productRepo) {
        this.orderRepository = orderRepository;
        this.usersRepository = usersRepository;
        this.productRepo = productRepo;
    }

    public Order createOrder(OrderRequest orderRequest,Authentication authentication) {
        Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));

        Order order = new Order();
        order.setUser(user);
        order.setUserAddress(orderRequest.getUserAddress());
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        List<OrderItem> orderItems = new ArrayList<>();

        BigDecimal totalAmount = BigDecimal.ZERO;
        if (orderRequest.getItems() == null ||
                orderRequest.getItems().isEmpty()) {

            throw new RuntimeException("Order must contain at least one item");
        }
        for(OrderItemRequest itemRequest : orderRequest.getItems() ){
            Product product = productRepo
                    .findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Item not found"));

            if (itemRequest.getQuantity() <= 0) {
                throw new RuntimeException("Invalid quantity");
            }

            BigDecimal price = product.getPrice();
            BigDecimal itemTotal = price.multiply(
                    BigDecimal.valueOf(itemRequest.getQuantity())
            );
            totalAmount = totalAmount.add(itemTotal);
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(price);

            orderItems.add(orderItem);
        }
        order.setTotalAmount(totalAmount);


        order.setItems(orderItems);

        return orderRepository.save(order);

    }

    public List<Order> getMyOrders(Authentication authentication) {
        Users user = usersRepository.findByEmail(authentication.getName()).orElseThrow(() -> new RuntimeException("User not found"));

       List<Order> orders = orderRepository.findByUser(user);

       if(orders.isEmpty())
           throw new RuntimeException("No orders available");

        return orders;
    }

    public Order getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        return order;
    }

    public Order cancelOrder(
            Long orderId,
            Authentication authentication
    ) {

        Users user = usersRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("You cannot cancel this order");
        }

        if (order.getStatus() == OrderStatus.SHIPPED ||
                order.getStatus() == OrderStatus.OUT_FOR_DELIVERY ||
                order.getStatus() == OrderStatus.DELIVERED) {

            throw new RuntimeException("Order cannot be cancelled");
        }

        order.setStatus(OrderStatus.CANCELLED);

        return orderRepository.save(order);
    }
}
