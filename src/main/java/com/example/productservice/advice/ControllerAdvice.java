package com.example.productservice.advice;

import com.example.productservice.dtos.ErrorDTO;
import com.example.productservice.exceptions.ProductNotFoundException;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorDTO> handleIllegalArgumentException(){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setErrorCode("400");
        errorDTO.setErrorMessage("Bad Request");

        return ResponseEntity.badRequest().body(errorDTO);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductNotFoundException(){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setErrorCode("404");
        errorDTO.setErrorMessage("Product Not Found");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
    }
}
