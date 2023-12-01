package com.fos.api.handler;

import com.fos.api.exception.ErrorInfo;
import com.fos.api.exception.ProcessFailureException;
import com.fos.api.exception.ValidationFailureException;
import com.fos.api.model.response.ExceptionResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiExceptionHandlerTest {


        @InjectMocks
        private ApiExceptionHandler apiExceptionHandler;

        @Test
        void handleValidateFailureException_shouldReturnBadRequestForDuplicateProductName() {
            ValidationFailureException ex = new ValidationFailureException(ErrorInfo.DUPLICATE_PRODUCT_NAME);
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleValidateFailureException(ex);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void handleValidateFailureException_shouldReturnBadRequestForInvalidProductId() {
            ValidationFailureException ex = new ValidationFailureException(ErrorInfo.INVALID_PRODUCT_ID);
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleValidateFailureException(ex);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        }
        @Test
        void handleValidateFailureException_shouldReturnBadRequestForRequestConstraintViolation() {
            ValidationFailureException ex = new ValidationFailureException(ErrorInfo.REQUEST_CONSTRAINT_VIOLATION);
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleValidateFailureException(ex);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void handleValidateFailureException_shouldReturnBadRequestForOtherValidationFailures() {
            ValidationFailureException ex = new ValidationFailureException(ErrorInfo.VALIDATION_FAILURE);
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleValidateFailureException(ex);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }

        @Test
        void handleProcessFailure_shouldReturnInternalServerError() {
            ProcessFailureException ex = new ProcessFailureException(ErrorInfo.PROCESS_FAILURE);
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleProcessFailure(ex);
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }

        @Test
        void handleValidationExceptions_shouldReturnBadRequestWithFieldErrors() {
            MethodArgumentNotValidException ex =mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            when(ex.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(new FieldError("fieldName", "field", "error")));
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleValidationExceptions(ex);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        }

        @Test
        void handleInvalidFormatException_shouldReturnBadRequestWithDefaultMessage() {
            HttpMessageNotReadableException ex = mock(HttpMessageNotReadableException.class);
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleInvalidFormatException(ex);
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        }

        @Test
        void handleException_shouldReturnInternalServerError() {
            Exception ex = new Exception("Some error");
            ResponseEntity<ExceptionResponse> response = apiExceptionHandler.handleException(ex);
            Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        }
}
