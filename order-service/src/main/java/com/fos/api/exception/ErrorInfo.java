package com.fos.api.exception;

public enum ErrorInfo {
    //Define Error Codes and messages
    REQUEST_CONSTRAINT_VIOLATION(1000, "Invalid Request Parameter"),
    INVALID_PRODUCT_ID(1001, "Invalid product Id"),
    DUPLICATE_PRODUCT_NAME(1002, "Product Name is already exists"),
    VALIDATION_FAILURE(1003, "Validation Failure"),
    PROCESS_FAILURE(2000, "Process Failure")
    ;
    private final int id;
    private final String message;

    ErrorInfo(int id, String message) {
        this.id = id;
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }
}
