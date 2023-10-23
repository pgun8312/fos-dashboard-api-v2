package com.fos.api.exception;

public class ValidationFailureException extends GenericRuntimeException{
    public ValidationFailureException(ErrorInfo errorInfo) {
        super(errorInfo);
    }
}
