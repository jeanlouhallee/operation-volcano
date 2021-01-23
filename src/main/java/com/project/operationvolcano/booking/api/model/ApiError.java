package com.project.operationvolcano.booking.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiError {
    int status;
    String error;
    String message;
}
