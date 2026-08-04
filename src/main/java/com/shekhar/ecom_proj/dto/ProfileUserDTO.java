package com.shekhar.ecom_proj.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProfileUserDTO {


    private String username;
    private String email;
    private String street;
    private String city;
    private String state;
    private String pinCode;

}
