package io.cloudmobility.tiago.utils;


import java.time.format.DateTimeParseException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ConversionFailedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleConversionFailedException(final RuntimeException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(new JsonErrorPayload(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleIllegalArgumentException(final RuntimeException ex) {
        return new ResponseEntity(new JsonErrorPayload(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDateTimeParseException(final RuntimeException ex) {
        final var customMessage = ex.getMessage() + ". Use the expected pattern: 'yyyy-MM-dd HH:mm'";
        log.error(customMessage);
        return new ResponseEntity(new JsonErrorPayload(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGeneralException(final RuntimeException ex) {
        final String customMessage = "Error processing the request...If error persists please email us at stuff@medical.com or contact us at 555111999";
        log.error(ex.getMessage());
        return new ResponseEntity(new JsonErrorPayload(customMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Data
    @RequiredArgsConstructor
    class JsonErrorPayload {
        final String message;
    }
}
