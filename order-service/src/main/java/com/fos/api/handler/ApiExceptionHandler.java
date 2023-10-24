package com.fos.api.handler;

import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ValidationFailureException.class)
    public final ResponseEntity<ExceptionResponse> handleValidateFailureException(ValidationFailureException ex) {


        //Mapping the ErrorCode to the HTTP status code(Basically managing the status code for each Validation Exception)

        if(ex.getErrorCode() == ErrorInfo.INVALID_ORDER_ID.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex.getErrorInfo()));
        }
        else if(ex.getErrorCode() == ErrorInfo.REQUEST_CONSTRAINT_VIOLATION.getId()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex.getErrorInfo()));
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ExceptionResponse(ex.getErrorInfo()));
        }
    }

    @ExceptionHandler(ProcessFailureException.class)
    public final ResponseEntity<ExceptionResponse> handleProcessFailure(ProcessFailureException ex) {

        if(ex.getErrorCode() == ErrorInfo.PROCESS_FAILURE.getId()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(ErrorInfo.PROCESS_FAILURE));
        }
        else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse((ErrorInfo.PROCESS_FAILURE)));
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public final ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        String errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage() )
                .collect(Collectors.joining(" , "));

        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ErrorInfo.VALIDATION_FAILURE.getId(),
                errorMessage
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionResponse);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleException(Exception ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                ErrorInfo.PROCESS_FAILURE.getId(),
                ErrorInfo.PROCESS_FAILURE.getMessage()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exceptionResponse);
    }
}
