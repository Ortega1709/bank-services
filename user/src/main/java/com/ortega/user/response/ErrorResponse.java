package com.ortega.user.response;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@SuperBuilder
public class ErrorResponse extends Response {
    private String error;
    private Map<String, String> errors;
}
