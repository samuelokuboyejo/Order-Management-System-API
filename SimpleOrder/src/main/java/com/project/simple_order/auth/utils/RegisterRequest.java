package com.project.simple_order.auth.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String firstName;

    private String lastName;

    private String password;

    private String phoneNumber;

    private String email;

    private String address;


}
