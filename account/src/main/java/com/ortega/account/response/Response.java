package com.ortega.account.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class Response {
    private String status;
    private Integer code;
    private String message;
}
