package com.project.project.main.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MainExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({AssessmentException.class})
    public ResponseEntity<Object> handleAssessmentException(){
        return new ResponseEntity<Object>("Post is currently rated", new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ObjectNotFoundException.class})
    public ResponseEntity<Object> handleObjectNotFoundException(){
        return new ResponseEntity<Object>("Object not found in database", new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleBusinessException(Exception ex, WebRequest request) {

        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}
