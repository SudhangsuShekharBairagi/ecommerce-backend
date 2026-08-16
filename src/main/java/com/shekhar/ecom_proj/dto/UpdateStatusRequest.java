package com.shekhar.ecom_proj.dto;

import com.shekhar.ecom_proj.enums.OrderStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateStatusRequest {

    private OrderStatus status;
}
