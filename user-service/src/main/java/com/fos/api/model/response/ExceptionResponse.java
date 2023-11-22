package com.fos.api.model.response;

import com.fos.api.exception.ErrorInfo;

public class ExceptionResponse {
    private int errorCode;
    private String errorDescription;

    public ExceptionResponse(int errorCode, String errorDescription) {
        this.errorCode = errorCode;
        this.errorDescription = errorDescription;
    }

    public ExceptionResponse(ErrorInfo errorInfo){
        this.errorCode = errorInfo.getId();
        this.errorDescription = errorInfo.getMessage();
    }
    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorDescription() {
        return errorDescription;
    }
}
