package com.biswacodes.secure_api_demo.rest;

public class EmployeeNotFoundException extends RuntimeException {
    
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
