package com.ipsator.foodOrderingSystem.payload;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public String anyExceptionHandler(Exception e, WebRequest w){
        return e.getMessage();
    }
}
