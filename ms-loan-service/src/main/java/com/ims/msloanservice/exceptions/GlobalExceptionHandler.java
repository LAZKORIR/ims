package com.ims.msloanservice.exceptions;


import com.ims.msloanservice.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse handleValidationExceptions(
            MethodArgumentNotValidException ex) {
       Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        ApiResponse response = new ApiResponse();
        response.setResponseDesc("An error occurred while processing your request, please try again later.");
        response.setResponseCode("GBC0001");
        response.setBody(errors);
        return response;
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse> handleRuntimeException(RuntimeException exception) {

        ApiResponse response = new ApiResponse();
        response.setResponseCode("GBC0001");
        response.setResponseDesc(exception.getMessage());
        response.setRequestRefID("REF-REXEC-0000000");
        response.setTransactionID("Runtime exception");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiResponse> handleNullException(NullPointerException exception) {

        System.out.println("NullPointerException");
        exception.printStackTrace();

        ApiResponse response = new ApiResponse();
        response.setResponseCode("GBC0001");
        response.setResponseDesc(exception.getMessage());
        response.setRequestRefID("REF-NP-0000000");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handleException(Exception exception) {

        ApiResponse response = new ApiResponse();
        response.setResponseCode("GBC0001");
        response.setResponseDesc(exception.getMessage());
        response.setRequestRefID("REF-EXECRUN-0000000");

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
