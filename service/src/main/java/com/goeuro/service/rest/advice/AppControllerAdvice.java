package com.goeuro.service.rest.advice;

import com.goeuro.api.model.ErrorInfoResponse;
import com.goeuro.service.exception.InvalidStationIdException;
import com.goeuro.service.exception.RoutesUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

@ControllerAdvice
public class AppControllerAdvice {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfoResponse handleResourceNotFoundException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage()).append("\n");
        }

        return ErrorInfoResponse.ErrorInfoResponseBuilder.anErrorInfo()
                .withCode(e.getClass().getSimpleName())
                .withMessage(strBuilder.toString())
                .build();
    }

    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfoResponse handleResourceNotFoundException(MissingServletRequestParameterException e) {
        return ErrorInfoResponse.ErrorInfoResponseBuilder.anErrorInfo()
                .withCode(e.getClass().getSimpleName())
                .withMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfoResponse handleNumberFormatException(NumberFormatException e) {
        return ErrorInfoResponse.ErrorInfoResponseBuilder.anErrorInfo()
                .withCode(e.getClass().getSimpleName())
                .withMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(InvalidStationIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorInfoResponse handleInvalidStationIdException(InvalidStationIdException e) {
        return ErrorInfoResponse.ErrorInfoResponseBuilder.anErrorInfo()
                .withCode(e.getClass().getSimpleName())
                .withMessage(e.getMessage())
                .build();
    }

    @ExceptionHandler(RoutesUnavailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public ErrorInfoResponse handleRoutesUnavailableException(RoutesUnavailableException e) {
        return ErrorInfoResponse.ErrorInfoResponseBuilder.anErrorInfo()
                .withCode(e.getClass().getSimpleName())
                .withMessage(e.getMessage())
                .build();
    }
}
