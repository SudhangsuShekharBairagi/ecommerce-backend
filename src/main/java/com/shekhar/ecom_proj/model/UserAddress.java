package com.shekhar.ecom_proj.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress {

    private String street;
    private String city;
    private String state;
    private String pinCode;
}
