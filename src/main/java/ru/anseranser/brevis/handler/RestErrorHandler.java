package ru.anseranser.brevis.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String error = exception.getBindingResult()
                .getFieldErrors()
                .getFirst()
                .getDefaultMessage();
        return new ResponseEntity<>(String.format("{\"error\" : \"%s\"}", error), HttpStatus.BAD_REQUEST);
    }
}
