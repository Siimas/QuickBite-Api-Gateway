package com.quickbite.gateway.Dto;

import lombok.Data;

@Data
public class SigninRequest {
    private String email;
    private String password;
}
