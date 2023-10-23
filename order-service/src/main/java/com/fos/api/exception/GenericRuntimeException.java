package com.fos.api.exception;

public class GenericRuntimeException extends RuntimeException{
    private final ErrorInfo errorInfo;
    private final String errorDescription;


    public GenericRuntimeException( ErrorInfo errorInfo) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
        this.errorDescription = errorInfo.getMessage();
    }

    public GenericRuntimeException(ErrorInfo errorInfo, String errorDescription) {
        super(errorInfo.getMessage());
        this.errorInfo = errorInfo;
        this.errorDescription = errorDescription;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    public int getErrorCode() {
        return errorInfo.getId();
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
