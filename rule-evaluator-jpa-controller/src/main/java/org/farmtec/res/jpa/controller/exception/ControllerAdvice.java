package org.farmtec.res.jpa.controller.exception;

import org.farmtec.res.service.exceptions.InvalidOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

/**
 * Created by dp on 14/03/2021
 */
@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(value = {InvalidOperation.class,NumberFormatException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorMessage handlerException(Exception e) {
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(),new Date(),
                e.getMessage(),
                e.getMessage());
    }
    @ExceptionHandler(value = {ResourceNotFound.class})
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorMessage handlerNotFoundException(ResourceNotFound e) {
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(),new Date(),
                e.getMessage(),
                e.getMessage());
    }
}
