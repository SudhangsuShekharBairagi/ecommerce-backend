package com.shekhar.ecom_proj.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemRequest {

    private Integer productId;

    private Integer quantity;
}
