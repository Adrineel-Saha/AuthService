package com.cognizant.authservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenValidationResponse {
    private boolean valid;
    private String userName;
    private String role;
}
