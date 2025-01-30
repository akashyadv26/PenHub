package com.PenHub.PenHub.dtos.ExceptionDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {

    private String feild;
    private String errorMessage;
}
