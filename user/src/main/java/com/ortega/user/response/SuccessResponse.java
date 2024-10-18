package com.ortega.user.response;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class SuccessResponse extends Response {
    private Object data;
}
