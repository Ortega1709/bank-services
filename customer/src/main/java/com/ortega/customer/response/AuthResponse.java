package com.ortega.customer.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class AuthResponse extends SuccessResponse {
    private String token;
}
