package com.shekhar.ecom_proj.dto;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shekhar.ecom_proj.enums.OrderStatus;
import com.shekhar.ecom_proj.model.OrderItem;
import com.shekhar.ecom_proj.model.UserAddress;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class UserOrderDto {
    private String orderNumber;

    private BigDecimal totalAmount;

    private OrderStatus status;

    private UserAddress userAddress;

    private LocalDateTime createdAt;

    private List<OrderItem> items;
}
