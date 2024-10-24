package com.ortega.account.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Getter
@SuperBuilder
public class ErrorResponse extends Response {
    private String error;
    private Map<String, String> errors;
}
