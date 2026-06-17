/* GlobalExceptionHandler */
package com.config;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.user.exception.EmailAlreadyExistsException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponseDto> buildAnswer(
            String message,
            HttpStatus status) {
        ErrorResponseDto error = new ErrorResponseDto(
                message,
                status.value(),
                LocalDateTime.now());

        return new ResponseEntity<>(error, status);
    }

    /* MODULO USER */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handlerEmailDuplicated(
            EmailAlreadyExistsException ex) {

        return buildAnswer(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST);
    }

    /* MODULO POST */
    /* MODULO COMMENT */

    /* GENERIC HANDLER */
    public ResponseEntity<ErrorResponseDto> handlerGeneric(Exception ex) {

        log.error("ERROR NO CONTROLADO DETECTADO: ", ex);
        return buildAnswer(
                "Algo salio inesperado, estaremos arreglandolo, consulte al equipo de soporte",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
