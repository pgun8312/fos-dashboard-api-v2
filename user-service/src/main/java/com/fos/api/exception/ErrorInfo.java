package com.fos.api.exception;

public enum ErrorInfo {
    //Define Error Codes and messages
    REQUEST_CONSTRAINT_VIOLATION(1000, "Invalid Request Parameter"),
    USER_ALREADY_EXISTS(1001, "User already exists"),
    USER_NAME_EXISTS(1002, "Username already exists. Please choose a different username."),
    VALIDATION_FAILURE(1003, "Validation Failure"),
    INVALID_USER_ID(1004, "Invalid user Id"),
    INVALID_EMAIL(1005, "Invalid email"),
    USER_PROFILE_EXISTS(1006, "User Profile already exists"),
    USER_PROFILE_NOT_EXISTS(1007, "User Profile not exists"),
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
