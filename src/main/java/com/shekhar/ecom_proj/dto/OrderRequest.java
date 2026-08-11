package com.shekhar.ecom_proj.dto;

import com.shekhar.ecom_proj.model.UserAddress;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequest {

    private UserAddress userAddress;

    private List<OrderItemRequest> items;
}
