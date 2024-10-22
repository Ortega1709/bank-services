package com.ortega.customer.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Response {
    private String status;
    private Integer code;
    private String message;
}
