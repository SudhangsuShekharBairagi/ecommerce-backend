package com.shekhar.ecom_proj.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {
    private Integer productId;
    private Integer quantity;
}
