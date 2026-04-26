package com.biswacodes.secure_api_demo.rest;

public class EmployeeErrorResponse {
    
    private int responseCode;
    private String message;
    private String timeStamp;

    public EmployeeErrorResponse() {
    }

    public EmployeeErrorResponse(int responseCode, String message, String timeStamp) {
        this.responseCode = responseCode;
        this.message = message;
        this.timeStamp = timeStamp;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }    
    
}
