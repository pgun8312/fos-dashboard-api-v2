package com.fos.api.exception;

public enum ErrorInfo {
    //Define Error Codes and messages
    REQUEST_CONSTRAINT_VIOLATION(1000, "Invalid Request Parameter"),
    INVALID_ORDER_ID(1001, "Invalid order Id"),
    INVALID_DATE_FORMAT(1002, "Invalid date format {yyyy-MM-dd HH:mm:ss}"),
    VALIDATION_FAILURE(1003, "Validation Failure"),
    INVALID_USER_ID(1004, "Invalid user Id"),
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
