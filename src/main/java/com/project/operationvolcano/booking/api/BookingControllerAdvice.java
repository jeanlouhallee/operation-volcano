package com.project.operationvolcano.booking.api;

import com.project.operationvolcano.booking.api.model.ApiErrorDto;
import com.project.operationvolcano.booking.exceptions.ReservationNotFoundException;
import com.project.operationvolcano.booking.exceptions.StayDatesNotAvailableException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class BookingControllerAdvice extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        // TODO Need to extract more information for error message (fieldname)
        List<String> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        String errorMsg = errors.toString();
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message(errorMsg).build();

        log.info("Exception occured: {}", errorMsg);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ReservationNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleReservationNotFoundException(ReservationNotFoundException ex) {

        String errorMsg = ex.getMessage();
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message(errorMsg).build();

        log.info("Exception occured: {}", errorMsg);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDto> handleConstraintViolationException(ConstraintViolationException ex) {

        String errorMsg = ex.getCause().getMessage();
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message(errorMsg).build();

        log.info("Exception occured: {}", errorMsg);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(StayDatesNotAvailableException.class)
    public ResponseEntity<ApiErrorDto> handleStayDatesNotAvailableException(StayDatesNotAvailableException ex) {

        String errorMsg = "These stay dates contain dates that are not available";
        ApiErrorDto error = ApiErrorDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .timestamp(LocalDateTime.now())
                .message(errorMsg).build();

        log.info("Exception occured: {}", errorMsg);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
