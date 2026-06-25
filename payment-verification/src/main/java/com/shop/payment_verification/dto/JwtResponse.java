package com.shop.payment_verification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class JwtResponse {
    private String token;
    private String role;
    private String name;
    private Long shopId;
}