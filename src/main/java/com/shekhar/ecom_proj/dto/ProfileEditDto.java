package com.shekhar.ecom_proj.dto;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileEditDto {
    private String username;
    private String street;
    private String city;
    private String state;
    private String pinCode;
}
