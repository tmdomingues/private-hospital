package io.cloudmobility.tiago.utils;

import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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
        final String customMsg;
        if (ex.getMessage().contains("fromIndex")) {
            customMsg = "Pagination out of bounds error: " + ex.getMessage();
        } else {
            customMsg = ex.getMessage();
        }
        log.error(customMsg);
        return new ResponseEntity(new JsonErrorPayload(customMsg), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDateTimeParseException(final RuntimeException ex) {
        final var customMessage = "Failed to deserialize date. Expected pattern: 'yyyy-MM-ddTHH:mm'";
        log.error(customMessage);
        return new ResponseEntity(new JsonErrorPayload(customMessage), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleBadCredentialsException(final RuntimeException ex) {
        log.error(ex.getMessage());
        return new ResponseEntity(new JsonErrorPayload(ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleNoSuchElementException(final RuntimeException ex) {
        final var customMessage = ex.getMessage() + ". No doctor/patient existing on the system with given id'";
        log.error(customMessage);
        return new ResponseEntity(new JsonErrorPayload(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @Data
    @RequiredArgsConstructor
    class JsonErrorPayload {
        final String message;
    }
}
