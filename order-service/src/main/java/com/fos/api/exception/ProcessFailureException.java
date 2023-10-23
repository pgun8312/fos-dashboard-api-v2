package com.fos.api.exception;

public class ProcessFailureException extends GenericRuntimeException{

    public ProcessFailureException(ErrorInfo errorInfo) {
        super(errorInfo);
    }

    public ProcessFailureException(ErrorInfo errorInfo, String errorDescription) {
        super(errorInfo, errorDescription);
    }
}
